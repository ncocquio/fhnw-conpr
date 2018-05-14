package ws.observables

import rx.lang.scala.Observable
import scala.concurrent.duration._
import ws.common.Student
import ws.common.EventoDB._

object AsyncEvento {

  def classMembers(): Observable[Student] =
    Observable.interval(FETCH_TIME_PER_ROW  millis).zip(Observable.from(conprStudents)).map(p => new Student(p._2))

  def estimateGrade(s: Student): Double =
    math.random * 2 + 4
}