package collections

object ParallelProblems {
  def main(args: Array[String]): Unit = {
    println("Undefined processing order")
    (1 to 10).par.foreach(i => println(Thread.currentThread + " " + i))
    
    println("Leading to non-deterministic results")
    println( (1 to 10).par.reduce(_ - _))
    println( (1 to 10).par.reduce(_ - _)) 
  }
}