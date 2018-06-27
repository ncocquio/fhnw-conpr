package le

import scala.concurrent.Await

object Self extends App {
  import akka.actor._
  val as = ActorSystem("as")

  case class Msg(msg: String, sender: ActorRef)

  class EchoActor extends Actor {
    def receive = { case Msg(msg, client) => client ! msg }
  }

  class Sender extends Actor {
    val echoActor = context.actorOf(Props[EchoActor])
    echoActor ! Msg("Hello", self)
    def receive = { case t => println(t) }
  }

  as.actorOf(Props[Sender])
}

object Sender extends App {
  import akka.actor.{ Actor, ActorSystem, Props }
  val as = ActorSystem("as")

  class EchoActor extends Actor {
    def receive = { case msg => sender ! msg } // reply to sender
  }

  class Sender extends Actor {
    val echoActor = context.actorOf(Props[EchoActor])
    echoActor ! "Hello"
    def receive = { case t => println(t) }
  }

  as.actorOf(Props[Sender])
}

object ReceiveTimeOut extends App {
  import akka.actor._; import scala.concurrent.duration._
  class TimeOutActor extends Actor {
    context.setReceiveTimeout(2 second)
    def receive = {
      case "Tick" => println("Tick")
      case ReceiveTimeout => println("TIMEOUT")
    }
  }

  val a = ActorSystem("as").actorOf(Props[TimeOutActor])
  
  a ! "Tick"
}

object ReceiveTimeOutReset extends App {
  import akka.actor.{ Actor, ActorSystem, Props }
  import akka.actor.ReceiveTimeout
  import scala.concurrent.duration._
  import akka.actor._; import scala.concurrent.duration._

  class TimeOutActor extends Actor {
    def receive = {
      case "Tick" =>
        println("Tick")
        context.setReceiveTimeout(1 second)
      case "Stop" =>
        println("Stop")
        context.setReceiveTimeout(Duration.Undefined)
      case ReceiveTimeout => println("TIMEOUT")
    }
  }

  val mysystem = ActorSystem("mysystem")
  val myactor = mysystem.actorOf(Props[TimeOutActor])

  myactor ! "Tick"
  myactor ! "Stop"
  Thread.sleep(2000)

  myactor ! "Tick"
  Thread.sleep(2000)
  myactor ! "Stop"
}

object Ask extends App {
  import akka.actor._
  import akka.pattern.ask // brings '?' into scope
  import akka.util.Timeout
  import scala.concurrent.Future
  import scala.concurrent.duration._

  class EchoActor extends Actor {
    def receive = { case msg => 
      //Thread.sleep(4000)
      
      sender ! msg }
  }

  val as = ActorSystem("as")
  val echoActor = as.actorOf(Props[EchoActor])

  implicit val timeout: Timeout = Timeout(3 seconds) // consumed by '?'
  val futResult: Future[String] = (echoActor ? "Hello").mapTo[String]

  val result = Await.result(futResult, 1 second) // Don't do that! Thread is blocked!
  println(result)
}

object AskWithCallback extends App {
  import akka.actor._
  import akka.pattern.ask // brings '?' into scope
  import akka.util.Timeout
  import scala.concurrent.Future
  import scala.concurrent.duration._

  class EchoActor extends Actor {
    def receive = { case msg => sender ! msg }
  }

  val as = ActorSystem("as")
  val echoActor = as.actorOf(Props[EchoActor])

  implicit val timeout = Timeout(3 seconds) // consumed by '?'
  val futResult: Future[String] = (echoActor ? "Hello").mapTo[String]

  import as.dispatcher // brings ExecutionContext into scope 
  futResult.map(_.toUpperCase).onSuccess {
    case result => println("Got result from Future: " + result)
  }
}

object AskWithPipeTo extends App {
  import akka.actor._
  import akka.pattern.ask // brings '?' into scope
  import akka.util.Timeout
  import scala.concurrent.Future
  import scala.concurrent.duration._

  import akka.pattern.pipe // additional to the previous imports 

  class ReverseActor extends Actor {
    def receive = { case msg: String => sender ! msg.reverse }
  }
  class ShoutActor extends Actor {
    def receive = { case msg: String => println(msg.toUpperCase) }
  }

  val as = ActorSystem("as")
  val reverseActor = as.actorOf(Props[ReverseActor])
  val shoutActor = as.actorOf(Props[ShoutActor])

  implicit val timeout = Timeout(3 seconds); import as.dispatcher
  val futResult = (reverseActor ? "Hello").mapTo[String]
  futResult pipeTo shoutActor

}



 

/*
// Reply
object _2_Ask extends App {
  class ReverseActor extends Actor {
    def receive = {
      case msg: String =>
        println("Received: " + msg)
        Thread.sleep(6000)
        sender ! msg.reverse
    }
  }
  import akka.pattern.{ ask, pipe }
  import system.dispatcher // The ExecutionContext that will be used
  import scala.concurrent.duration._
  import akka.util.Timeout
  implicit val timeout = Timeout(5 seconds)

  val system = ActorSystem("MySystem")
  val reverseactor = system.actorOf(Props[ReverseActor])
  //val answerFut0 = ask(reverseactor, "Hello")(timeout).mapTo[String]
  //val answerFut1 = (reverseactor ask "Hello").mapTo[String]
  val answerFut2 = (reverseactor ? "Hello").mapTo[String]

  try {
    val res = Await.result(answerFut2, Duration.Inf)
    println(res)
  } catch {
    case te: AskTimeoutException => println("AskTimeoutException")
  }
}

object _2_PipeTo extends App {
  class ReverseActor extends Actor {
    def receive = {
      case msg: String =>
        println("Received: " + msg)
        sender ! msg.reverse
    }
  }

  class ShoutActor extends Actor {
    def receive = {
      case msg: String =>
        println("Received: " + msg.toUpperCase)
    }
  }
  import akka.pattern.{ ask, pipe }
  import system.dispatcher // The ExecutionContext that will be used
  import scala.concurrent.duration._
  import akka.util.Timeout
  implicit val timeout = Timeout(5 seconds)

  val system = ActorSystem("MySystem")
  val reverseActor = system.actorOf(Props[ReverseActor])
  val shoutActor = system.actorOf(Props[ShoutActor])

  val f = for {
    s1 <- (reverseActor ? "Hello").mapTo[String]
    s2 <- (reverseActor ? "World").mapTo[String]
  } yield s1 + s2
  val fut = f pipeTo shoutActor
}

object FutureExample extends App {
  import akka.actor._
  import akka.pattern.{ ask, pipe }
  import akka.util.Timeout
  import scala.concurrent.Future
  import scala.concurrent.duration._

  class EchoActor extends Actor {
    def receive = { case msg => sender ! msg }
  }

  val as = ActorSystem("as")
  val echoActor = as.actorOf(Props[EchoActor])
  implicit val timeout = Timeout(3 seconds)
  val futureResult: Future[String] = (echoActor ? "Hello").mapTo[String]

  import as.dispatcher

  futureResult.map(_.toUpperCase)

  val res = Await.result(futureResult, 2 seconds)
  println(res)
}

*/
