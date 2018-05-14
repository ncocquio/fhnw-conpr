package primitives

import java.lang.{ThreadLocal => JavaThreadLocal}

class ThreadLocal[T](override val initialValue: T) extends JavaThreadLocal[T] {
  def apply(): T = get
  def update(t: T) = set(t)
}

object ThreadLocal {

  def main(args: Array[String]) = {
    val tl = new ThreadLocal[String]("")
    async { tl() = "T1"; Thread.sleep(500); println(tl()) }
    async { tl() = "T2"; Thread.sleep(200); println(tl()) }
    
    val x = new ThreadLocal[Int](44)
    println(x())
  }
  
  def async(action: => Unit) = {
    new Thread() { override def run = { action } }.start
  }

}
