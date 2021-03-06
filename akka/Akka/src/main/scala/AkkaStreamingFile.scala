import java.io.InputStream
import java.io.File

import akka.actor._

case class ProcessStringMsg(lineNumber: Int, fileName: String, string: String, fileSender: Option[ActorRef], listener: ActorRef)
case class StringProcessedMsg(fileSender: Option[ActorRef])
case class FileReference(fileName: String, stream: InputStream)
case class CaptureStream(fileName: String, numOfWords: Int, lineNumber: Int)
case class closeStream(totalTime: Long, fileName: String)
case class StartProcessFileMsg()


class StringCounterActor extends Actor {
  def receive = {
    case ProcessStringMsg(lineNumber, fileName, string, rootSender, listener) => {
      var wordsInLine = 0
      if(string.length != 0)
      {
        wordsInLine = string.split(" ").length
      }

      try {
        listener ! CaptureStream(fileName, wordsInLine, lineNumber) //Streams word count to listener
        sender ! StringProcessedMsg(rootSender) //Sends a ping to the RoutingActor every time it finishes a task
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



class RoutingActor(fileRef: FileReference, listener: ActorRef) extends Actor {

  private var running = false
  private var totalLines = 0
  private var linesProcessed = 0
  private val fileName = fileRef.fileName
  private var startTime = 0L

  def receive = {
    case StartProcessFileMsg() => {
      if (running) {
        println("Warning: duplicate start message received")
      } else {
        running = true
        startTime = System.nanoTime()
        val rootSender = Some(sender) // save reference to process invoker
        val lines = scala.io.Source.fromInputStream(fileRef.stream)
        lines.getLines.foreach { line =>
          context.actorOf(Props[StringCounterActor]) ! ProcessStringMsg(totalLines, fileName, line, rootSender, listener)
          totalLines += 1
        }
      }
    }
    case StringProcessedMsg(rootSender) => {
      linesProcessed += 1

      if (linesProcessed == totalLines) {
        val stopTime = System.nanoTime()
        listener ! closeStream(stopTime-startTime, fileName)
        rootSender match {
          case (Some(o)) => o ! linesProcessed // provide result to process invoker
        }
      }
    }
    case _ => println("message not recognized!")
  }
}

object AkkaWordCounter extends App {

  import akka.util.Timeout
  import scala.concurrent.duration._
  import akka.pattern.ask
  import akka.dispatch.ExecutionContexts._

  override def main(args: Array[String]) {

    val files = getListOfFiles("src/main/resources/")

    /**
      * foreach takes a procedure -- a function with a result type Unit -- as the right operand.
      * It simply applies the procedure to each List element.
      * The result of the operation is again Unit; no list of results is assembled.
      */
    files.foreach(initActorSystem)

  }

  def initActorSystem(fileName: String): Unit = {
    //Fixing bug from original code: https://www.toptal.com/scala/concurrency-and-fault-tolerance-made-easy-an-intro-to-akka#comment-1776147740
    implicit val executionContext = global
    val system = ActorSystem("ActorSystem")
    // create the result listener, which will print the result
    val listener = system.actorOf(Props[Listener], name = "Listener")
    //Load from /resources folder: http://stackoverflow.com/questions/27360977/how-to-read-files-from-resources-folder-in-scala
    val stream : InputStream = getClass.getResourceAsStream("/" + fileName)
    val actor = system.actorOf(Props(new RoutingActor(new FileReference(fileName, stream), listener)))
    implicit val timeout = Timeout(5 seconds)
    //When the future returns the OnCompleteSignal is sent
    val futureResult = actor ? StartProcessFileMsg()
    futureResult.map { result =>
      println("Number of lines processed in " + fileName + ": " + result)
      //Terminate Actor System when result is received
      system.terminate()
    }

  }

  def getListOfFiles(dir: String):List[String] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.map(file => file.getName).toList
    } else {
      List[String]()
    }
  }
}

class Listener extends Actor {
  private var totalNumberOfWords = 0

  def receive = {

    case CaptureStream(fileName, numOfWords, lineNumber) =>
      totalNumberOfWords += numOfWords
    //println(fileName + " " + "L." + lineNumber + " " + numOfWords + " words")
    //Stream results to Client

    case closeStream(totalTime, fileName) =>
      println("Stream Complete: " + fileName + " Total Number of Words: " + totalNumberOfWords +
        " Total Time: " + totalTime/1000000 + "ms")

    case _ => println("Error: message not recognized")
  }
}