package collections
// head, tail, indexed, ::, map, filter, reduce, zip, groupBy, find
object Lists {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val l = List("Hello", "World", "!")             //> l  : List[String] = List(Hello, World, !)

  l.head                                          //> res0: String = Hello
	l.tail.head                               //> res1: String = World
	val ll = "bla" :: l                       //> ll  : List[String] = List(bla, Hello, World, !)
  l                                               //> res2: List[String] = List(Hello, World, !)
  
  
  val sizes = l.map(w => w.size)                  //> sizes  : List[Int] = List(5, 5, 1)
  sizes.filter(i => i > 2)                        //> res3: List[Int] = List(5, 5)
  
  sizes.reduce((l, r) => l+r)                     //> res4: Int = 11
  
  l.zip(List(1, 2, 3))                            //> res5: List[(String, Int)] = List((Hello,1), (World,2), (!,3))
  
  val days = List("Mo", "Mi", "Do")               //> days  : List[String] = List(Mo, Mi, Do)
  days.groupBy(d => d.charAt(0))                  //> res6: scala.collection.immutable.Map[Char,List[String]] = Map(D -> List(Do),
                                                  //|  M -> List(Mo, Mi))
 
  days.find(d => d.charAt(1) == 'x')              //> res7: Option[String] = None
}