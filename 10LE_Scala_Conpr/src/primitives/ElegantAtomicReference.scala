package primitives

import scala.annotation.tailrec
import java.util.concurrent.atomic.AtomicReference

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

object AtomicTest extends App {
  val nice = new ElegantAtomicReference(42)
  nice.modify(_ + 1)
}
