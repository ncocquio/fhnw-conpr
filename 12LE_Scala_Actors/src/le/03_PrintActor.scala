package le

object PrintActor1 extends App {
  import akka.actor.{ Actor, ActorSystem, Props }
  val as = ActorSystem("as")

  class PrintActor extends Actor {
    def receive = { case msg => println("received msg: " + msg) }
  }

  val printActor = as.actorOf(Props[PrintActor])

  printActor ! "hello"
  printActor ! 4711
}

object PrintActor2 extends App {
  import akka.actor.{ Actor, ActorSystem, Props }
  val as = ActorSystem("as")

  val f: PartialFunction[Any, Unit] = {
    case msg => println("received msg: " + msg)
  }

  class PrintActor extends Actor {
    def receive = f
  }

  val printActor = as.actorOf(Props[PrintActor])

  printActor ! "hello"
  printActor ! 4711
}

object PrintActor3 extends App {
  import akka.actor.{ Actor, ActorSystem, Props }
  val as = ActorSystem("as")

  class PrintActor extends Actor {
    def receive = new PartialFunction[Any, Unit] {
      override def isDefinedAt(x: Any) =
        x.isInstanceOf[Int] && x.asInstanceOf[Int] % 2 == 0
      override def apply(x: Any) = println("received msg: " + x)
    }
  }

  val printActor = as.actorOf(Props[PrintActor])

  printActor ! "hello"
  printActor ! 4711
  printActor ! 4712
}

object PrintActor4 extends App {
  import akka.actor.{ Actor, ActorSystem, Props }
  val as = ActorSystem("as")

  class PrintActor extends Actor {
    def receive = {
      case x: Int if x % 2 == 0 => println("received msg: " + x)
    }
  }

  val printActor = as.actorOf(Props[PrintActor])

  printActor ! "hello"
  printActor ! 4711
  printActor ! 4712
}