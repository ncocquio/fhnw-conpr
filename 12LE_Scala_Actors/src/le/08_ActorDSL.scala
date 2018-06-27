package le

object DSL extends App {
  import akka.actor.ActorDSL._
  import akka.actor.ActorSystem
  implicit val system = ActorSystem("system")

  val a = actor(new Act {
    become {
      case "hello" => sender ! "hi"
    }
  })

  val b = actor(new Act {
    become {
      case "start" => a ! "hello"
      case "hi" => println("Got: hi")
    }
  })

  b ! "start"
}