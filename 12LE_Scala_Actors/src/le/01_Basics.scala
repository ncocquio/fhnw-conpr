package le

import scala.concurrent.Await
import scala.concurrent.duration.Duration


// Run many times to observer different print orderings.
object Intro extends App {
  import akka.actor.{ ActorSystem, Actor, Props, ActorRef }

  val as = ActorSystem("as") // Actor infrastructure

  class PrintActor extends Actor { // Actor definition
    var nthRequest = 0 // Mutable state
    def receive = {
      case msg: String => // Behavior           
        nthRequest += 1
        println(nthRequest + ":" + msg)
    }
  }

  val printActor: ActorRef = as.actorOf(Props[PrintActor]) // Actor creation
  
  Thread.sleep(1000)
  printActor ! "Hello"
  println("Sent Hello")
  printActor ! "Bye"
  println("Sent Bye")
  
  Await.ready(as.terminate(), Duration.Inf)
}

