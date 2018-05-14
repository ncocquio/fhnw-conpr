package other

import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContextExecutorService
import java.util.concurrent.TimeUnit

/* Implicit entfernen und explizit übergeben. */
object ImplicitParameter extends App {

  implicit val ec: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))

  def myFuture(block: => Unit)(implicit c: ExecutionContext): Unit =
    c.execute(new Runnable() { override def run = block })

  myFuture {
    println(Thread.currentThread())
  }

  ec.shutdown()
  ec.awaitTermination(10, TimeUnit.SECONDS)

}

object ImplicitClasses extends App {
  implicit class CrazyString(s: String) {
    def rot13: String = s map {
      case c if 'a' <= c.toLower && c.toLower <= 'm' => c + 13 toChar
      case c if 'n' <= c.toLower && c.toLower <= 'z' => c - 13 toChar
      case c => c
    }
  }
  
  println("Hello".rot13)
  println("Uryyb".rot13)
}