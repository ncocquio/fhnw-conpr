package other

object ByName extends App {
  def test(block: => Unit): Unit = {
    println("before block")
    block
    println("after block")
  }
  test{
    println("block")
  }
}

object ByValue extends App {
  def test(block: Unit): Unit = {
    println("before block")
    block
    println("after block")
  }
  test(println("block"))
}

object Funtion0 extends App {
  def test(block: () => Unit): Unit = {
    println("before block")
    block()
    println("after block")
  }
  test(() => println("block"))
}