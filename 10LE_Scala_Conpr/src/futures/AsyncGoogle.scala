package futures

import scala.io.Source
import scala.io.Codec
import scala.concurrent._
import scala.concurrent.duration._
// import ExecutionContext.Implicits.global
import java.net.UnknownHostException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import scala.io.StdIn
import scala.util.Success
import scala.util.Failure

object ScalaFutures extends App { 
  
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))
  
  /* Synchrone, IO-blockierende Methode. */
  def loadURL(url: String): String = 
    Source.fromURL(url)(Codec.UTF8).getLines().mkString
  
  /* Indexiert den übergebenen String. */
  def indexContent(content: String): Map[String,Int] =
    content.split("\\W").groupBy(x => x).mapValues(_.size)
  
  /* Lädt den Inhalt der URL asynchron! */
  val futureContent: Future[String] = 
    Future { loadURL("https://www.scala-lang.org/") }
  
  /* Operiert auf den Daten (wendet indexContent an) obwohl das Resultat des Futures
   * möglicherweise noch gar nicht vorhanden ist. DAS HIER IST DER TOLLE PART!
   */
  val futureIndex: Future[Map[String,Int]] = 
    futureContent.map(indexContent)
 
  /* Registriert Callback um notifiziert zu werden, wenn das Resultat fertig ist. */
  futureIndex.onComplete{ 
    case Success(m) => println("Result: " + m) 
    case Failure(t: UnknownHostException) => println("Host not found: " + t.getMessage()) 
    case Failure(t) => println("Unknown Problem: " + t.getMessage()) 
  } 

  /* Man kann aber weiterhin blockierend warten bis das Resultat bereit ist. */
  val result = Await.result(futureIndex, 20 seconds)
  
  StdIn.readLine("Press Enter to exit")
  ec.shutdown()
  ec.awaitTermination(1, TimeUnit.MINUTES)
}
