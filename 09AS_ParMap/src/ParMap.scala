import java.util.concurrent._

import scala.concurrent.ExecutionContext

class ParMap {
  def parMap[A, B](l: List[A], f: A => B): List[B] = {
    val ex = Executors.newCachedThreadPool()
    val exc = ExecutionContext.fromExecutor(ex)

    val tasks: List[Future[B]] = l.map(x =>
      ex.submit(new Callable[B] {
        override def call(): B = {
          f(x)
        }
    }))

    val results = tasks.map(fu => fu.get())

    ex.shutdown()

    results
  }

  def main(args: Array[String]): Unit = {
    parMap(List(1,2,3), (i:Int) => i > 1)
  }
}
