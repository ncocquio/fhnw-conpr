package ws.common

case class Student(val email: String) 

case class GradedStudent(val student: Student, val grade: Double)