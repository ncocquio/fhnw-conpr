package other

object PartialFunctions extends App {
  val pf: PartialFunction[Int,String] = {
    case 42 => "forty two"
    case i if i > 100 => "okay"
  }
  
  println(pf(42))
  println(pf(101))
  println(pf.isDefinedAt(43))
  println(pf(43))
}