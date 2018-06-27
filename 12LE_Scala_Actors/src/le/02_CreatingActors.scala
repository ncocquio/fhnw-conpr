package le

object _01_DefaultConstructor extends App {
  import akka.actor._
  val as = ActorSystem("as")

  class PrintActor extends Actor {
    def receive = { case msg => println(msg) }
  }

  val printActor: ActorRef = as.actorOf(Props[PrintActor])

  printActor ! "msg"
  printActor ! "hi"
}

object _02_DontUseNew extends App {
  import akka.actor.Actor

  class PrintActor extends Actor {
    def receive = { case msg => println(msg) }
  }

  new PrintActor
}

object _03_NonDefaultConstructor extends App {
  import akka.actor._
  class PrintActor(pre: String) extends Actor {
    def receive = {
      case msg => println(pre + msg)
    }
  }

  val as = ActorSystem("as")
  val actor = as.actorOf(Props(new PrintActor("Msg:")))
  actor ! "Hi"
}

object _04_AnonymousActorSubclass extends App {
  import akka.actor._
  val as = ActorSystem("as")
  val print: ActorRef = as.actorOf(Props(
    new Actor {
      def receive = { case msg => println(msg) }
    }))

  print ! "Hi"
}

