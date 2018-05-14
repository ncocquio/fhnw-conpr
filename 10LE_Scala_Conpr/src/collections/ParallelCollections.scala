package collections

object ParallelCollections extends App {
  
   val dnaSeq = io.Source.fromFile("na_armU.dmel.RELEASE5").getLines().drop(1).mkString
                                                           .grouped(3).toVector.take(500000)
   
   println("DNA: " + dnaSeq.take(4))
                                                           
   val dnaPar = dnaSeq.par
   
   // Thymin -> Uracil
   val rnaSeq = time("seq transcription"){
     dnaSeq.map(codon => codon.map(base => if(base == 'T') 'U' else base))
  }
   
  println("RNA: " + rnaSeq.take(4))
  
  val rnaPar = time("par transcription"){
     dnaPar.map(codon => codon.map(base => if(base == 'T') 'U' else base))
  }
  
  val pheSeq = time("seq translation"){
    rnaSeq.filter(_.matches("U.U")).map(_.charAt(1))
  }
  
  println("Extract: " + pheSeq.take(4))
  
   val phePar = time("par translation"){
    rnaPar.filter(_.matches("U.U")).map(_.charAt(1))
  }


  def time[T](msg: String)(c: => T): T = {
    c /*JIT*/
    c /*JIT*/
    val start = System.nanoTime();
    val result = c;
    val duration = System.nanoTime() - start;
    val scaledDuration = if (duration < 10000) duration + "ns"
    else if (duration < 10000000) duration / 1000 + "micros"
    else duration / 1000000 + "ms"
    println(msg + " elapsed time: " + scaledDuration)
    result
  }
}