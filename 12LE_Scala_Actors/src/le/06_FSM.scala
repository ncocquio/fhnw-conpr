package le

object FSM extends App {
  import akka.actor.{ Actor, ActorSystem, Props }
  case object On
  case object Off

  class Switch extends Actor {
    var on = false
    def receive = {
      case On if !on =>
        println("turned on"); on = true
      case Off if on =>
        println("turned off"); on = false
      case _ => println("ignore")
    }
  }

  val as = ActorSystem("as")
  val switch = as.actorOf(Props[Switch])

  switch ! On
  switch ! On
}

object FSM2 extends App {
  import akka.actor.{ Actor, ActorSystem, Props }
  case object On; case object Off

  class Switch extends Actor {
    val offBehavior: PartialFunction[Any,Unit] = {
      case On => println("turned on"); context.become(onBehavior) 
      case _ =>  println("ignore")
    }
    
    val onBehavior: PartialFunction[Any,Unit] = {
      case Off => println("turned off"); context.become(offBehavior) 
      case _ =>  println("ignore")
    }
    
    def receive = offBehavior
  }

  val as = ActorSystem("as")
  val switch = as.actorOf(Props[Switch])

  switch ! On
  switch ! On
}

object FSM3 extends App {
  import akka.actor.{ Actor, ActorSystem, Props }
  case object On; case object Off

  class Switch extends Actor {
    val offBehavior: PartialFunction[Any,Unit] = {
      case On => println("turned on"); context.become(onBehavior, false) 
      case _ =>  println("ignore")
    }
    
    val onBehavior: PartialFunction[Any,Unit] = {
      case Off => println("turned off"); context.unbecome() 
      case _ =>  println("ignore")
    }
    
    def receive = offBehavior
  }

  val as = ActorSystem("as")
  val switch = as.actorOf(Props[Switch])

  switch ! On
  switch ! On
}