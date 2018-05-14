package collections

/** The most important things to do with Scala's immutable collections! */
object ImmutableCollections extends App {
  
  /** List operations */
  val list0 = List(1,2,1)                    // List(1,2,1) 
  
  val head = list0.head                      // 1
  
  val tail = list0.tail                      // List(2,1)
  
  val list1 = 5 :: list0                     // List(5,1,2,1)
  
  val list2 = Nil                            // List()
  
  val list3 = list0.map(i => i + 1)          // List(2,3,2)

  val list4 = list0.filter(i => i > 1)       // List(2)

  val sum = list0.reduce((x,y) => x+y)       // 4
  
  val list5 = list0.zip(List('A', 'B', 'C')) // List((1,A), (2,B), (1,C))
  
  val list6 = list0.groupBy(i => i % 2 == 0) // Map(false -> List(1,1), true -> List(2))

  val large = list0.find(i => i > 12)        // None
  
  val small = list0.find(i => i < 12)        // Some(1)
  
  list0.foreach(i => print(i + " "))         // 1 2 1

  
  
  /** Set operations */
  val set0 = Set(1,2,3,2)            // Set(1,2,3)
  
  val set1 = set0 + 4                // Set(1,2,3,4)
  
  val set2 = set0 - 1                // Set(2,3)
  
  val contains0 = set1(0)            // false
  
  val set3 = set1.filter(i => i > 2) // Set(3,4)
  
  val set4 = set1.map(i => i > 2)    // Set(false,true)

  
  
  /** Map operations */
  val map0 = Map(1 -> "one", 2 -> "two")  // Map(1 -> "one", 2 -> "two")
  
  val map1 = map0 + (3 -> "three")        // Map(1 -> "one", 2 -> "two", 3 -> "three")
  
  val map2 = set0 - 1                     // Map(2 -> "two", 3 -> "three")
  
  val val1 = map0(1)                      // "one"
  
  val val0 = map0(0)                      // java.util.NoSuchElementException: key not found: 0
  
  val optVal0 = map0.get(0)               // None
  
  val optVal1 = map0.get(1)               // Some(1)
  
  val res = map1.filter(kv => kv._1 > 2)  // Map(3 -> "three")
}