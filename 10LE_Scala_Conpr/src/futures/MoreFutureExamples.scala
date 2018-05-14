package futures



import scala.io.Source
import scala.io.Codec
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import java.net.UnknownHostException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object MoreFutureExamples extends App {
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(3))
  
  def info(msg: String): Unit = println(msg + ": " + Thread.currentThread())
  
  val f1: Future[Int] = Future{ info("f1"); 1 }
  val f2: Future[Int] = Future{ info("f2"); 2 }
  val f3: Future[Int] = f1.flatMap(i => f2.map(j => { info("f3"); i + j }))
  
  val f3FOR: Future[Int] = for{
    i <- f1
    j <- f2
  } yield i + j
  
  val result = Await.result(f3FOR, 1 seconds)
  println(result)
  
  ec.shutdown()
  ec.awaitTermination(1, TimeUnit.MINUTES)
}

object Exceptions {
  def main(args: Array[String]): Unit = {
    Future(throw new IllegalArgumentException).foreach(println _)
  }
}
