package par

import java.util.concurrent.Executors
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.ExecutorService

object ParMap extends App {
  def myParMap[A, B](l: List[A], f: A => B): List[B] = {
    val ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    val futures: List[Future[B]] = l.map(a => ex.submit(new Callable[B]() { def call = f(a) }))
    val result: List[B] = futures.map(f => f.get)
    ex.shutdown()
    result
  }
   
  def theirParMap[A,B](l: List[A], f: A => B): List[B] =
    l.par.map(f).toList
   
     
  myParMap(List(1,2,3), (i: Int) => i + 1)
   
  theirParMap(List(1,2,3),  (i: Int) => i + 1)


  // Lösung von Silvan Laube 
  def parMap[A,B](l: List[A], f:A => B, pool: ExecutorService = Executors.newCachedThreadPool()) : List[B] = l match {
    case Nil => Nil
    case x :: xs =>
      val future = pool.submit(new Callable[B]() {
        def call(): B = f(x)
      })
      future.get() :: parMap(xs, f, pool) 
  }
}