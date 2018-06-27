package le

object CaseClassMessages1 extends App {
  import akka.actor.{ Actor, ActorSystem, Props }

  case class PrintMsg(msg: String)
  case class ShoutMsg(msg: String)


  class PrintActor extends Actor {
    def receive = {
      case PrintMsg(m) => println("received: " + m)
      case ShoutMsg(m) => println("RECEIVED: " + m.toUpperCase)
    }
  }

  ActorSystem("as").actorOf(Props[PrintActor]) ! ShoutMsg("Hello")
}








object CaseClassMessages2 extends App {
  import akka.actor.{ Actor, ActorSystem, Props }

  case class PrintMsg(msg: String)
  case class ShoutMsg(msg: String)
  case class Both(pm: PrintMsg, sm: ShoutMsg, i: Int)

  class PrintActor extends Actor {
    def receive = {
      case PrintMsg(m) => 
        println("received: " + m)
      case ShoutMsg(m) => 
        println("RECEIVED: " + m.toUpperCase)
      case Both(PrintMsg(p), ShoutMsg(s), myInt) => 
        println(s"received: $p ${s.toUpperCase}" ); println(myInt)
    }
  }

  ActorSystem("as").actorOf(Props[PrintActor]) ! Both(PrintMsg("Hello"), ShoutMsg("World"), 3)
}