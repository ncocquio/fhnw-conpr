package le

import akka.dispatch.ExecutionContexts
import java.util.concurrent.Executor


object ActorsAndThreads extends App {
  import akka.actor.{ ActorSystem, Actor, Props }
  
  def info(msg: String): Unit =
    println(Thread.currentThread().getName() + ": " + msg)
  
  class PrintActor extends Actor {
    def receive = { case msg: String => info(msg) }
  }
  
  val as = ActorSystem("as")

  val p1 = as.actorOf(Props[PrintActor], "p1")
  val p2 = as.actorOf(Props[PrintActor], "p2")
  
  info("Sending message")
  p1 ! "P1: Hi"
  p2 ! "P2: Hi"
  p1 ! "P1: Bye"
  p2 ! "P2: Bye"
}

object ActorsAndThreads2 extends App {
  import akka.actor.{ ActorSystem, Actor, Props }
  
  class PrintActor(id: Int) extends Actor {
    def receive = { 
      case _ if id == 0 => 
        println("Actor: " + id + " " +Thread.currentThread().getName())
      }
  }
  
  val as = ActorSystem("as")
  
  val actors = 0 to 19 map (i => as.actorOf(Props(new PrintActor(i)), "p"+i))
  (0 to 200).par.foreach (i => actors(i % 20) ! "MSG")
}

object UnhandledMessage extends App {
  import akka.actor.{ ActorSystem, Actor, Props, UnhandledMessage }
  
  class PrintActor extends Actor {
    def receive = { 
      case "ONLY THIS MSG" => println("Got it") }
  }
  
  val as = ActorSystem("as")
  
  class Listener extends Actor {
    def receive = {
      case d: UnhandledMessage => println(d)
    }
  }
  
  val listener = as.actorOf(Props[Listener])
  as.eventStream.subscribe(listener, classOf[UnhandledMessage])
  
  Thread.sleep(1000)
  
  val a = as.actorOf(Props[PrintActor])
  a ! 12
}

object Lookup extends App {
  import akka.actor.{ ActorSystem, Actor, Props }

  class B extends Actor {
    def receive = { case "B" => println("B") }
  }

  class A extends Actor {
    context.actorOf(Props[B], "b1")
    context.actorOf(Props[B], "b2")

    def receive = { case "A" => println("A") }
  }

  val as = ActorSystem("as")
  val a = as.actorOf(Props[A], "a")
  val b = as.actorSelection("akka://as/user/a/b1")
  b ! "B"

}

object Supervision extends App {
  import akka.actor.{ ActorSystem, Actor, Props }
  import scala.concurrent.duration._
  import akka.actor.SupervisorStrategy._
  import akka.actor.OneForOneStrategy
  class A extends Actor {
    override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: ArithmeticException => Restart
      case _: IllegalStateException => Restart
    }

    val b1 = context.actorOf(Props(new B(2)), "b1")
    val b2 = context.actorOf(Props(new B(0)), "b2")

    def receive = {
      case "Start" =>
        println("A")
        b1 ! 5
        b2 ! 4
    }
  }

  class B(div: Int) extends Actor {
    override def preRestart(reason: Throwable, message: Option[Any]) = println("preRestart")
    def receive = { case i: Int => sender ! (i / div) }
  }

  val mysystem = ActorSystem("mysystem")
  val a = mysystem.actorOf(Props[A], "A")

  a ! "Start"
  //a ! "Start"
}