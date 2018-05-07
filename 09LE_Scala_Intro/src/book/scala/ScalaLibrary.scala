package book.scala

object ScalaLibrary {
  
  case class Book(author: String, title: String)
  
  val books = List(Book("A", "A2"), Book("A","A1"),
                   Book("B","B1"))
 
  def booksByAuthor(author: String): List[Book] = 
    books.filter(b => b.author == author)
    
  def main(args: Array[String]): Unit = 
    println(booksByAuthor("A").sortBy(b => b.title))
}