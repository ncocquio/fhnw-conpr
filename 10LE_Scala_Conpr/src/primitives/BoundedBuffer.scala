package primitives

class BoundedBuffer[E](capacity: Int) {
  private val lock = new Object
  private var e: List[E] = Nil;
  
  def put(a: E) = when(e.size < capacity) { e = a :: e }
  
  def get: E = when(!e.isEmpty) {
    val tmp = e.head; e = e.tail
    tmp
  }
  
  private def when[T](cond: => Boolean)(code: => T): T = { 
    lock.synchronized {
      while(!cond) try{ lock.wait() } catch { case e: InterruptedException => }
      val result = code; lock.notifyAll(); result
    }
  }
}


object BoundedBufferTest extends App {
  val buffer = new BoundedBuffer[Int](2)
  
  new Thread(){ 
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
