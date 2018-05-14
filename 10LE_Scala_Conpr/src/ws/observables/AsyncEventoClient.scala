package ws.observables

import AsyncEvento._
import ws.common.Student
import rx.lang.scala.Observable
import ws.common.GradedStudent
import scala.io.StdIn

object AsyncEventoClient {
  
  def main(args: Array[String]) : Unit = { 
    val startTime = System.currentTimeMillis()
   
    val students = classMembers()
    
    students.subscribe(student => println(s"[${System.currentTimeMillis() - startTime} ms] ${student.email}"))
  
    // a)
    val gradedStudents = ???
    
    // b)
    val talents = ???
    
    // c)
    ???
    StdIn.readLine()
  }
  
  /* Gibt die Top 10 Studierenden aus */
  def printTopTen(talents: Observable[GradedStudent]) {
    println("Top 10")
    val top10 = talents.toBlocking.toList.sortBy(gs => gs.grade)(Ordering.Double.reverse).take(10)
    val ordered = top10.zipWithIndex.map(p => s"[${p._2 + 1}]\t${p._1.student.email} (${p._1.grade})")
    ordered.foreach(println(_))
  }
}
