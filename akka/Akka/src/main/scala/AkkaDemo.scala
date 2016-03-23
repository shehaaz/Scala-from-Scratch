import java.io.InputStream

import akka.actor.{ Actor, ActorRef, Props, ActorSystem }

case class ProcessStringMsg(string: String)
case class StringProcessedMsg(words: Integer)

class StringCounterActor extends Actor {
  def receive = {
    case ProcessStringMsg(string) => {
      val wordsInLine = string.split(" ").length
      sender ! StringProcessedMsg(wordsInLine)
    }
    case _ => println("Error: message not recognized")
  }
}

case class StartProcessFileMsg()

class WordCounterActor(stream: InputStream) extends Actor {

  private var running = false
  private var totalLines = 0
  private var linesProcessed = 0
  private var result = 0
  private var fileSender: Option[ActorRef] = None

  def receive = {
    case StartProcessFileMsg() => {
      if (running) {
        println("Warning: duplicate start message received")
      } else {
        running = true
        fileSender = Some(sender) // save reference to process invoker
        val lines = scala.io.Source.fromInputStream(stream)
        lines.getLines.foreach { line =>
          System.out.println(line)
          context.actorOf(Props[StringCounterActor]) ! ProcessStringMsg(line)
          totalLines += 1
        }
      }
    }
    case StringProcessedMsg(words) => {
      result += words
      linesProcessed += 1
      if (linesProcessed == totalLines) {
        fileSender.foreach(_ ! result) // provide result to process invoker
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
    val system = ActorSystem("System")
    //Load from /resources folder: http://stackoverflow.com/questions/27360977/how-to-read-files-from-resources-folder-in-scala
    val stream : InputStream = getClass.getResourceAsStream("/text.txt")
    val actor = system.actorOf(Props(new WordCounterActor(stream)))
    implicit val timeout = Timeout(25 seconds)
    val future = actor ? StartProcessFileMsg()
    future.map { result =>
      println("Total number of words " + result)
      system.terminate()
    }
  }
}