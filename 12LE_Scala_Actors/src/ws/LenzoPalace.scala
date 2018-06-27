package ws

import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.Await
import scala.concurrent.duration.Duration
/**
 * In dieser Aufgabe implementieren ein Döner Restaurant mittels Aktoren.
 *
 * Spezifikation:
 * - Ein Kellner (Waiter) nimmt von einem Kunden Bestellungen entgegen. Eine Bestellung ist
 *   immer eine Kombination aus einem Getränk (Drink) und einer Speise (Food).
 *
 * - Ein Getränk kostet 3 CHF, ein Kebab kostet 8 CHF und ein Dürüm kostet 9 CHF.
 *
 * - Der Kellner schickt dem Kunde direkt ein Glas (Glass) des gewünschten Getränks zurück.
 *   Die Speise aber muss der Koch zubereiten. Der Kellner schickt also dem Koch den Auftrag
 *   für den Kunden die Speise zuzubereiten.
 *
 * - Der Koch schickt dem Kunde direkt ein Teller (Plate) der gewünschten Speise.
 *
 * Aufgaben:
 *
 * a) Implementieren Sie die Klasse Waiter.
 * b) Implementieren Sie die Klasse Cook.
 * c) [optional] Erweitern Sie die Waiter Aktor so, dass er sich das Umsatztotal merkt. Und dann
 *    implmentieren Sie den Boss Aktor, der das Total beim Waiter abfragt.
 *
 * @see http://www.lenzo-palace.ch/
 *
  * Lösung: Siehe ganz unten
 */
object LenzoPalace {
  // Getränke
  trait Drink
  case object Coke extends Drink // 3 CHF
  case object IceTea extends Drink // 3 CHF

  // Mahlzeiten
  trait Food
  case object Döner extends Food // 8 CHF
  case object Dürüm extends Food // 9 CHF

  // Customer -> Waiter
  case class Order(food: Food, drink: Drink, customer: ActorRef)
  // Waiter -> Customer
  case class Glass(drink: Drink)
  // Waiter -> Cook
  case class FoodOrder(food: Food, customer: ActorRef)
  // Cook -> Customer
  case class Plate(food: Food)
  // Boss -> Waiter
  case class RequestTotal(boss : ActorRef)
  case class GiveTotal(tot: Int)


  class Waiter(cook: ActorRef) extends Actor {
    var total:Int = 0

    def receive = {
      case Order(food, drink, customer) => {
        cook ! FoodOrder(food, customer)
        customer ! Glass(drink)
      }
    }
  }

  class Cook extends Actor {
    def receive = {
      case FoodOrder(food, customer) => {
        customer ! Plate(food)
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val as = ActorSystem("as")

    val cook = as.actorOf(Props[Cook])
    val waiter = as.actorOf(Props(new Waiter(cook)))

    // Anonymer Kunde
    as.actorOf(Props(new Actor {
      waiter ! Order(Dürüm, IceTea, self)

      def receive = {
        case Glass(IceTea) => println("Sluuurp")
        case Plate(Dürüm) => println("Hmmm! Delicious!!")
      }
    }))
    
    Thread.sleep(100)
    Await.ready(as.terminate(), Duration.Inf)
  }
}


object Solutions {
  implicit class CrazyString(s: String) {
    def rot13: String = s map {
      case c if 'a' <= c.toLower && c.toLower <= 'm' => c + 13 toChar
      case c if 'n' <= c.toLower && c.toLower <= 'z' => c - 13 toChar
      case c => c
    }
  }
  
  val waiter = """ 
    pynff Jnvgre(pbbx: NpgbeErs) rkgraqf Npgbe {
    
      ine gbgny: Vag = 0

      qrs erprvir = {
        pnfr Beqre(sbbq, qevax) =>
          gbgny += (vs (sbbq == Xrono) 8 ryfr 9)
          gbgny += 3

          pbbx ! SbbqBeqre(sbbq, fraqre)
          fraqre ! Tynff(qevax)

        pnfr Gbgny => fraqre ! gbgny
      }
    }
  """
    
  val cook = """ 
    pynff Pbbx rkgraqf Npgbe {
      qrs erprvir = {
        pnfr SbbqBeqre(sbbq, phfgbzre) =>
          phfgbzre ! Cyngr(sbbq)
      }
    } 
  """
    
  def main(args: Array[String]): Unit = {
    println("Uvre svaqra Fvr qvr Yöfhatra" rot13)
  }
}
 