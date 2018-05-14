package ws.collections

import rx.lang.scala.Observable
import scala.concurrent.duration._
import ws.common.Student
import ws.common.EventoDB._

object SyncEvento {

  def classMembers(): List[Student] = delayed(conprStudents.length * FETCH_TIME_PER_ROW) {
    conprStudents.map(e => new Student(e))
  }
 
  def estimateGrade(s: Student): Double =
    math.random * 2 + 4
  
  // Delays the evaluation of block by millis milliseconds
  def delayed[T](millis: Int)(block: => T): T = {
    Thread.sleep(millis)
    block
  }
}