package ws.collections

import SyncEvento._
import ws.common.Student
import ws.common.GradedStudent
import ws.common.GradedStudent
import ws.common.GradedStudent
import ws.common.GradedStudent

object SyncEventoClient {

  def main(args: Array[String]): Unit = {
    val startTime = System.currentTimeMillis()
    
    val students: List[Student] = classMembers()
    
    students.foreach(student => println(s"[${System.currentTimeMillis() - startTime} ms] ${student.email}"))
    
    // a)
    val gradedStudents = ???
    
    // b)
    val talents = ???
    
    // c)
    ???
  }
  
  /* Gibt die Top 10 Studierenden aus */
  def printTopTen(talents: List[GradedStudent]) {
    println("Top 10")
    val top10 = talents.sortBy(gs => gs.grade)(Ordering.Double.reverse).take(10)
    val ordered = top10.zipWithIndex.map(p => s"[${p._2 + 1}]\t${p._1.student.email} (${p._1.grade})")
    ordered.foreach(s => println(s))
  }
}
