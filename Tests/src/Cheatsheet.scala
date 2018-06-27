val someSms = SMS("12345", "Are you there?")
val someVoiceRecording = VoiceRecording("Tom", "voicerecording.org/id/123")

def showNotification(notification: Notification): String = {
  notification match {
    case Email(email, title, _) =>
      s"You got an email from $email with title: $title"
    case SMS(number, message) =>
      s"You got an SMS from $number! Message: $message"
    case VoiceRecording(name, link) =>
      s"you received a Voice Recording from $name! Click the link to hear it: $link"
  }
}

/* Interface Person */
trait Person {
  /*
  - Abstrakte Methode (Signatur ohne Rumpf) (wie abstrakte Methode in Java)
  - Parameter msg hat den Typ String
  - Return Typ der Funktion ist Unit (wie void in Java)
  - Definitionen in Scala: Name ':' Typ
  */
  def speak(msg: String): Unit
}

abstract class Notification

/* Class Hacker
 - name und age sind Konstruktorparameter und Variablen auf einen Streich
 - val ist immutable (final)
 - var ist mutable
 - Definitionen in Scala: Name ':' Typ
*/
class Hacker(val name: String, var age: Int) extends Person {
  /* Konstruktor Code */
  println("Constructing Hacker: " + toString())

  def speak(msg: String): Unit = {
    /* Semikolon ';' erlaubt aber nicht notwendig */
    val slang: String = msg.toUpperCase().replace('E', '3').replace('T', '7').replace('L', '1')
    /* Wie 'System.out.println' in Java */
    println(name + "#" + slang)
  }

  /*
  - override um Methode zu überschreiben
  - 'return' ist nicht notwendig. Das Resultat des letzten Ausdrucks wird zurückgegeben
  - Geschwungene Klammern '{}' sind nicht notwendig für einen einzelnen Ausdruck
  */
  override def toString(): String = name + " " + age
}

case class Email(sender: String, title: String, body: String) extends Notification

case class SMS(caller: String, message: String) extends Notification

case class VoiceRecording(contactName: String, link: String) extends Notification

class BoundedBuffer[E](capacity: Int) {
  private val lock = new Object
  private var e: List[E] = Nil;

  def put(a: E) = when(e.size < capacity) {
    e = a :: e
  }

  def get: E = when(!e.isEmpty) {
    val tmp = e.head;
    e = e.tail
    tmp
  }

  private def when[T](cond: => Boolean)(code: => T): T = {
    lock.synchronized {
      while (!cond) try {
        lock.wait()
      } catch {
        case e: InterruptedException =>
      }
      val result = code;
      lock.notifyAll();
      result
    }
  }
}

// prints You got an SMS from 12345! Message: Are you there?
println(showNotification(someSms))
// you received a Voice Recording from Tom! Click the link to hear it: voicerecording.org/id/123
println(showNotification(someVoiceRecording))

class ElegantAtomicReference[E](e: E) {
  val atomic = new AtomicReference[E](e)

  @tailrec
  final def modify(f: E => E): E = {
    val currentValue = atomic.get()
    val newValue = f(currentValue)
    if (!atomic.compareAndSet(currentValue, newValue)) modify(f)
    else newValue
  }

  def get(): E = atomic.get()
}

class ThreadLocal[T](override val initialValue: T) extends JavaThreadLocal[T] {
  def apply(): T = get

  def update(t: T) = set(t)
}

/* Singleton Instanz
- Alle Members darin sind wie 'static' in Java
*/
object ScalaCheatSheet {
  /* main Methode (Einstiegspunkt)
  - Array ist ein normaler Collection Typ
  - Zwischen den eckigen Klammern stehen Typparameter (Java: List<String>)
  */
  def main(args: Array[String]): Unit = {
    /* val ist eine 'final' Variable */
    val sophia: Hacker = new Hacker("Sophia", 16)
    /* age ist mutable und kann somit verändert werden */
    sophia.age = 8
    sophia.speak("MeltDown")
    /* Der Typ kann häufig inferiert werden */
    val nicolai = new Hacker("Nicolai", 6)
    val crew = List(sophia, nicolai)
    /* Aufruf von foreach mit einer anonymen Funktion */
    crew.foreach(h => h.speak("I am " + h.age + " years old"))
    var maxAge: Int = 0
    /* Wie in Java 'for(a : as) {...}' */
    for (hacker <- crew) {
      /* if ist ein Ausdruck mit Resultat wie 'cond ? a : b' in Java */
      maxAge = if (hacker.age > maxAge) hacker.age else maxAge
    }
    var cnt = 1
    /* while Schleifen sind wie in Java */
    while (cnt <= nicolai.age) {
      println("Happy Birthday " + cnt)
      cnt += 1
    }
  }
}

object PartialFunctions extends App {
  val pf: PartialFunction[Int, String] = {
    case 42 => "forty two"
    case i if i > 100 => "okay"
  }

  println(pf(42))
  println(pf(101))
  println(pf.isDefinedAt(43))
  println(pf(43))
}

object BoundedBufferTest extends App {
  val buffer = new BoundedBuffer[Int](2)

  new Thread() {
    override def run = {
      Thread.sleep(3000)
      println("Putting value")
      buffer.put(42)
    }
  }.start

  println("Calling get")
  val result = buffer.get
  println("Got: " + result)
}

object AtomicTest extends App {
  val nice = new ElegantAtomicReference(42)
  nice.modify(_ + 1)
}

object ThreadLocal {
  def main(args: Array[String]) = {
    val tl = new ThreadLocal[String]("")
    async {
      tl() = "T1";
      Thread.sleep(500);
      println(tl())
    }
    async {
      tl() = "T2";
      Thread.sleep(200);
      println(tl())
    }
    val x = new ThreadLocal[Int](44)
    println(x())
  }

  def async(action: => Unit) = {
    new Thread() {
      override def run = {
        action
      }
    }.start
  }
}

object _01_RefCompile {
  val r1 = Ref(1)
  val r2: Ref[Int] = Ref(1)

  //val insideR1 = r1() // Does NOT compile
  //r2() = 13 // Does NOT compile

  atomic { implicit tx =>
    r1.set(11)
    r1() = 13

    val i = r1.get
    val j = r1()
  }
}

// r1() = 13 == r1.update(13)
object _02_SingleOperationTransactions {
  val ref: Ref[Int] = Ref(1)
  val refView: Ref.View[Int] = ref.single
  refView.transform(_ + 1)
  refView() = 42 // Write

  val insideRef = refView() // Read
}

object _03_Isolation extends App {
  val r = Ref(1)
  thread {
    atomic { implicit tx =>
      println("Tx1: About to change Ref")
      r() = 12
      println("Tx1: Changed ref to " + r())
      Thread.sleep(3000)
      println("Tx1: About to commit")
    }
  }
  atomic { implicit tx =>
    println("Tx2: Starting Transaction")
    while (r() != 12) {
      println("Tx2: Read " + r())
      Thread.sleep(400)
    }
    println("Tx2: Read " + r())
  }
}

object _04_AtomicityExceptions extends App {
  val r = Ref(1)
  atomic { implicit tx =>

    r() = 2
    try {

      atomic { implicit tx =>
        println("Within tx 1: " + r())
        r() = 13
        println("Within tx 1: " + r())
        throw new IllegalStateException()
      }

    } catch {
      case e: IllegalStateException => println("After exception: " + r.single())
    }

    println("" + r())

  }
}

object _05_AfterCommit extends App {
  atomic { implicit tx =>
    println("Start outer tx")
    atomic { implicit tx =>
      println("Start inner tx")
      Txn.afterCommit(_ => println("After commit"))
      println("End inner tx")
    }

    Txn.afterCommit(_ => println("After commit2"))
    println("End outer tx")
  }
}

object _06_AfterRollback extends App {
  atomic { implicit tx =>
    println("Start outer tx")
    atomic { implicit tx =>
      println("Startinner tx")
      Txn.afterRollback(_ => println("After rollback"))
      println("End inner tx")
    }
    throw new IllegalStateException("Huston we've got a pr...")
    println("End outer tx")
  }
}

object Util {
  def thread(b: => Unit): Thread = {
    val t = new Thread() {
      override def run(): Unit = b
    }
    t.start
    t
  }
}


object Intro extends App {
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


object _01_DefaultConstructor extends App {
  val as = ActorSystem("as")

  class PrintActor extends Actor {
    def receive = {
      case msg => println(msg)
    }
  }

  val printActor: ActorRef = as.actorOf(Props[PrintActor])
  printActor ! "msg"
  printActor ! "hi"
}

object _03_NonDefaultConstructor extends App {

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
  val as = ActorSystem("as")
  val print: ActorRef = as.actorOf(Props(
    new Actor {
      def receive = {
        case msg => println(msg)
      }
    }))
  print ! "Hi"
}


object CaseClassMessages1 extends App {

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

  case class PrintMsg(msg: String)
  case class ShoutMsg(msg: String)
  case class Both(pm: PrintMsg, sm: ShoutMsg, i: Int)

  class PrintActor extends Actor {
    def receive = {
      case PrintMsg(m) => println("received: " + m)
      case ShoutMsg(m) => println("RECEIVED: " + m.toUpperCase)
      case Both(PrintMsg(p), ShoutMsg(s), myInt) =>
        println(s"received: $p ${s.toUpperCase}"); println(myInt)
    }
  }

  ActorSystem("as").actorOf(Props[PrintActor]) ! Both(PrintMsg("Hello"), ShoutMsg("World"), 2)
}


object Self extends App {
  val as = ActorSystem("as")

  case class Msg(msg: String, sender: ActorRef)

  class EchoActor extends Actor {
    def receive = {
      case Msg(msg, client) => client ! msg
    }
  }

  class Sender extends Actor {
    val echoActor = context.actorOf(Props[EchoActor])
    echoActor ! Msg("Hello", self)

    def receive = {
      case t => println(t)
    }
  }

  as.actorOf(Props[Sender])
}

object Sender extends App {
  val as = ActorSystem("as")

  class EchoActor extends Actor {
    def receive = {
      case msg => sender ! msg
    } // reply to sender
  }

  class Sender extends Actor {
    val echoActor = context.actorOf(Props[EchoActor])
    echoActor ! "Hello"

    def receive = {
      case t => println(t)
    }
  }

  as.actorOf(Props[Sender])
}

object ReceiveTimeOut extends App {

  import scala.concurrent.duration._

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

  import scala.concurrent.duration._

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

  import scala.concurrent.Future
  import scala.concurrent.duration._

  class EchoActor extends Actor {
    def receive = {
      case msg =>
        //Thread.sleep(4000)

        sender ! msg
    }
  }

  val as = ActorSystem("as")
  val echoActor = as.actorOf(Props[EchoActor])
  implicit val timeout: Timeout = Timeout(3 seconds) // consumed by '?'
  val futResult: Future[String] = (echoActor ? "Hello").mapTo[String]
  val result = Await.result(futResult, 1 second) // Don't do that! Thread is blocked!
  println(result)
}

object AskWithCallback extends App {

  import scala.concurrent.Future
  import scala.concurrent.duration._

  class EchoActor extends Actor {
    def receive = {
      case msg => sender ! msg
    }
  }

  val as = ActorSystem("as")
  val echoActor = as.actorOf(Props[EchoActor])
  implicit val timeout = Timeout(3 seconds) // consumed by '?'
  val futResult: Future[String] = (echoActor ? "Hello").mapTo[String] // brings ExecutionContext into scope
  futResult.map(_.toUpperCase).onSuccess {
    case result => println("Got result from Future: " + result)
  }
}

object AskWithPipeTo extends App {

  import scala.concurrent.duration._ // additional to the previous imports
  class ReverseActor extends Actor {
    def receive = {
      case msg: String => sender ! msg.reverse
    }
  }

  class ShoutActor extends Actor {
    def receive = {
      case msg: String => println(msg.toUpperCase)
    }
  }

  val as = ActorSystem("as")
  val reverseActor = as.actorOf(Props[ReverseActor])
  val shoutActor = as.actorOf(Props[ShoutActor])
  implicit val timeout = Timeout(3 seconds);
  val futResult = (reverseActor ? "Hello").mapTo[String]
  futResult pipeTo shoutActor
}


object LockExample extends App {
  val mailboxConf = ConfigFactory.parseString(
    """
    stash-dispatcher {
      mailbox-type = "akka.dispatch.UnboundedDequeBasedMailbox"
    }
  """)
  val system = ActorSystem("system", mailboxConf)

  // API
  case class Lock(id: String)

  case class Unlock(id: String)

  class LockActor extends Actor with Stash {
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
      case l@Lock(id) => {
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
    def receive = {
      case msg => println(msg)
    }
  }

  val a = as.actorOf(Props[A])
  a ! "A"
  a ! PoisonPill // nice terminate
  a ! "A"
  //a ! Kill
  // a ! "A"
}

object UnhandledMessageExample extends App {
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

