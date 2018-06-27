package ws
import scala.concurrent.stm._
import Util.thread
import java.util.concurrent.atomic.AtomicInteger

/* Aufgaben:
 * 1. Notieren Sie die erwarteten Ausgaben.
 * 2. Lassen Sie das Programm laufen. Haben sich Ihre Erwartungen bestätigt?
 */
object _05_AtomicityExceptions extends App {
  val r = Ref(1)
  var nonManaged = 0
  
  atomic { implicit tx =>

    try {
      println(s"a:  ${r()} $nonManaged")
      r() = 13
      nonManaged = 13
      println(s"b: ${r()} $nonManaged")

      atomic { implicit tx =>
        println(s"c: ${r()} $nonManaged")
        r() = 26
        nonManaged = 26
        println(s"d: ${r()} $nonManaged")
        throw new IllegalStateException()
      }

    } catch {
      case ex: IllegalStateException => 
        println(s"e: ${r()} $nonManaged")
    }

    println(s"f: ${r()} $nonManaged")
  }
  
  println("--------")
  println(s"g: ${r.single()} $nonManaged")
}

/* Aufgaben:
 * 1. Notieren Sie die erwarteten Ausgaben. Wann wird das "After commit" ausgegeben.
 * 2. Lassen Sie das Programm laufen. Haben sich Ihre Erwartungen bestätigt?
 */
object _06_AfterCommit extends App {
  
  atomic { implicit tx =>
    println("Start outer tx")

    atomic { implicit tx =>
      println("Start inner tx")
      Txn.afterCommit(_ => println("After commit"))
      println("End inner tx")
    }

    println("End outer tx")
  }

  println("Done")
}
