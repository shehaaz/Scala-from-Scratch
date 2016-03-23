package helloworld

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

/**
  * Created by Shehaaz on 3/23/16.
  */
class HelloActor extends Actor {
  def receive = {
    case "hello" => println("hello back at you")
    case _       => println("huh?")
  }
}

object Main extends App {
  val system = ActorSystem("HelloSystem")
  // default Actor constructor
  val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")
  System.out.println("Saying Hello")
  helloActor ! "hello"
  System.out.println("Saying buenos dias")
  helloActor ! "buenos dias"
  System.out.println("THIS IS NON-BLOCKING")
}
