package observables

import scala.concurrent.Future
import rx.lang.scala.Observable
import rx.lang.scala.JavaConversions
import scala.io.Source
import scala.io.Codec
import scala.concurrent.ExecutionContext.Implicits.global


object Comparison {
  
  /* Single result, synchronous */
  def getSyncWebPage(url: String): String = 
    Source.fromURL(url)(Codec.UTF8).getLines().mkString
  
  /* Single result, asynchronous */
  def getAsyncWebPage(url: String): Future[String] = Future {
     getSyncWebPage(url)
  }
  
  /* Multiple result, synchronous */
  def getSyncWebPages(urls: List[String]): List[String] = {
    urls.map(getSyncWebPage)
  }
  
  /* Multiple result, asynchronous */
  def getAsyncWebPages(urls: List[String]): Observable[String] = {
    Observable.from(urls.map(getAsyncWebPage)).flatMap(Observable.from(_))
  }
  
  val page = "http://scala-lang.org/"
  val pages = List("http://scala-lang.org/", "http://akka.io/", "http://rxscala.github.io/")
}