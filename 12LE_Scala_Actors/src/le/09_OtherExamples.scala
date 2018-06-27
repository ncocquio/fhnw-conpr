package le

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging
import scala.concurrent.Await

object LockExample extends App {
  import akka.actor._
  import com.typesafe.config.ConfigFactory

  val mailboxConf = ConfigFactory.parseString("""
    stash-dispatcher {
      mailbox-type = "akka.dispatch.UnboundedDequeBasedMailbox"
    }
  """)

  val system = ActorSystem("system", mailboxConf)

  // API
  case class Lock(id: String)
  case class Unlock(id: String)

  class LockActor extends Actor with Stash {
    import context._
    var owner: Option[String] = None

    def receive = unlocked

    def unlocked: PartialFunction[Any, Unit] = {
      case Lock(id) => {
        println("Lock: " + id)
        owner = Some(id)
        become(locked)
      }
      case m => println("Illegal message: " + m)
    }

    def locked: PartialFunction[Any, Unit] = {
      case Unlock(id) if owner.get == id => {
        println("Unlock: " + id)
        owner = None
        become(unlocked)
        unstashAll()
      }
      case Unlock(id) if owner.get != id => {
        println("Not lock owner: " + id)
      }
      case l @ Lock(id) => {
        println("Stash: " + l)
        stash() // other lock commands
      }
    }
  }

  val lock = system.actorOf(Props[LockActor].withDispatcher("stash-dispatcher"))

  lock ! Lock("A")
  lock ! Unlock("A")

  lock ! Lock("A")

  new Thread() {
    override def run = {
      lock ! Lock("B")
      Thread.sleep(4000)
      lock ! Unlock("B")
    }
  }.start

  Thread.sleep(3000)

  lock ! Unlock("A")
}

object StoppingExample extends App {
  import akka.actor._

  val as = ActorSystem("as")

  val listener = as.actorOf(Props(new Actor {
    def receive = {
      case d: UnhandledMessage => println(d)
      case d: DeadLetter => println(d)
    }
  }))
  as.eventStream.subscribe(listener, classOf[UnhandledMessage])
  as.eventStream.subscribe(listener, classOf[DeadLetter])

  class A extends Actor {
    def receive = { case msg => println(msg) }
  }
  val a = as.actorOf(Props[A])
  a ! "A"
  a ! PoisonPill // nice terminate
  a ! "A"

  //a ! Kill
  // a ! "A"
}

object UnhandledMessageExample extends App {
  import akka.actor._
  val as = ActorSystem("as")

  val listener = as.actorOf(Props(new Actor {
    def receive = {
      case d: UnhandledMessage => println(d)
    }
  }))
  as.eventStream.subscribe(listener, classOf[UnhandledMessage])

  val actor = as.actorOf(Props(new Actor {
    def receive = {
      case i: Int => println(i)
    }
  }))

  actor ! "A "
}



