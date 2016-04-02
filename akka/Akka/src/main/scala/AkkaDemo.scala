import java.io.InputStream

import akka.actor.{ Actor, ActorRef, Props, ActorSystem }

case class ProcessStringMsg(lineNumber: Int, fileName: String, string: String, fileSender: Option[ActorRef], listener: ActorRef)
case class StringProcessedMsg(words: Integer, fileSender: Option[ActorRef])
case class FileReference(fileName: String, stream: InputStream)
case class ProcessedFile(fileName: String, totalNumWords: Int, timeElapsed: Long, onCompleteSignal: Boolean)
case class CaptureStream(fileName: String, numOfWords: Int, lineNumber: Int, onCompleteSignal: Boolean)

class StringCounterActor extends Actor {
  def receive = {
    case ProcessStringMsg(lineNumber, fileName, string, fileSender, listener) => {
        var wordsInLine = 0
        if(string.length != 0)
          {
            wordsInLine = string.split(" ").length
          }
        try {
          listener ! CaptureStream(fileName, wordsInLine, lineNumber, false)
          sender ! StringProcessedMsg(wordsInLine, fileSender)
        }
        catch {
          case e: Exception =>
            sender ! akka.actor.Status.Failure(e)
            throw e
        }
    }
    case _ => println("Error: message not recognized")
  }
}

case class StartProcessFileMsg()

class WordCounterActor(fileRef: FileReference, listener: ActorRef) extends Actor {

  private var running = false
  private var totalLines = 0
  private var linesProcessed = 0
  private val fileName = fileRef.fileName
  private var startTime = 0L
  private var totalNumOfWords = 0

  def receive = {
    case StartProcessFileMsg() => {
      if (running) {
        println("Warning: duplicate start message received")
      } else {
        running = true
        startTime = System.nanoTime()
        val fileSender = Some(sender) // save reference to process invoker
        val lines = scala.io.Source.fromInputStream(fileRef.stream)
        lines.getLines.foreach { line =>
          context.actorOf(Props[StringCounterActor]) ! ProcessStringMsg(totalLines, fileName, line, fileSender, listener)
          totalLines += 1
        }
      }
    }
    case StringProcessedMsg(wordsInLine, fileSender) => {
      totalNumOfWords += wordsInLine
      linesProcessed += 1

      if (linesProcessed == totalLines) {
        val stopTime = System.nanoTime()
        listener ! CaptureStream(fileName, totalNumOfWords, totalLines, true)
        fileSender match {
          case (Some(o)) => o ! new ProcessedFile(fileName, totalNumOfWords, stopTime-startTime, true) // provide result to process invoker
        }
      }
    }
    case _ => println("message not recognized!")
  }
}

object Sample extends App {

  import akka.util.Timeout
  import scala.concurrent.duration._
  import akka.pattern.ask
  import akka.dispatch.ExecutionContexts._

  override def main(args: Array[String]) {
    //Fixing bug from original code: https://www.toptal.com/scala/concurrency-and-fault-tolerance-made-easy-an-intro-to-akka#comment-1776147740
    implicit val ec = global

    val bookSystem = ActorSystem("BookSystem")
    // create the result listener, which will print the result and shutdown the system
    val bookListener = bookSystem.actorOf(Props[Listener], name = "bookListener")
    //Load from /resources folder: http://stackoverflow.com/questions/27360977/how-to-read-files-from-resources-folder-in-scala
    val bookStream : InputStream = getClass.getResourceAsStream("/book.txt")
    val bookActor = bookSystem.actorOf(Props(new WordCounterActor(new FileReference("book.txt", bookStream), bookListener)))
    implicit val timeout = Timeout(1 seconds)
    val futurebook = bookActor ? StartProcessFileMsg()
    futurebook.map { result =>
      println("Elapsed time: " + result.asInstanceOf[ProcessedFile].timeElapsed / 1000000 + "ms. " +
        "FileName " + result.asInstanceOf[ProcessedFile].fileName +
        ". Total number of words " + result.asInstanceOf[ProcessedFile].totalNumWords)

      if(result.asInstanceOf[ProcessedFile].onCompleteSignal){
        //Terminate Actor System
        bookSystem.terminate()
      }
    }

    val textSystem = ActorSystem("TextSystem")
    // create the result listener, which will print the result and shutdown the system
    val textListener = textSystem.actorOf(Props[Listener], name = "textListener")
    val textStream : InputStream = getClass.getResourceAsStream("/text.txt")
    val textActor = textSystem.actorOf(Props(new WordCounterActor(new FileReference("text.txt", textStream), textListener)))
    val futuretext = textActor ? StartProcessFileMsg()
    futuretext.map { result =>
      println("Elapsed time: " + result.asInstanceOf[ProcessedFile].timeElapsed / 1000000 + "ms. " +
              "FileName " + result.asInstanceOf[ProcessedFile].fileName +
              ". Total number of words " + result.asInstanceOf[ProcessedFile].totalNumWords)
      if(result.asInstanceOf[ProcessedFile].onCompleteSignal){
        //Terminate Actor System
        textSystem.terminate()
      }
    }
  }

  class Listener extends Actor {
    def receive = {

      case CaptureStream(fileName, numOfWords, lineNumber, onCompleteSignal) =>
                            if(!onCompleteSignal){
                              println(fileName + " " + "L." + lineNumber + " " + numOfWords)
                            }
                            else{
                              println("Stream Complete " + fileName)
                            }

      case _ => println("Error: message not recognized")
    }
  }
}