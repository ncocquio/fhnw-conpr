package irc

import java.io.{BufferedReader, InputStreamReader, OutputStreamWriter}
import java.net.Socket

import rx.lang.scala.observables.ConnectableObservable
import rx.lang.scala.schedulers.IOScheduler
import rx.lang.scala.{Observable, Subscriber}

import scala.io.StdIn

object RxIrcBot {
  val host = "chat.freenode.org"
  val port = 6667
  val chan = "#conpr_4iab"
  val nick = "spasti_2"
  val real = "spasti_2"
  // set to 'true' to see received and transmitted messages 
  val logMessages = true

  def main(args: Array[String]): Unit = {
    val socket = new Socket(host, port)
    println(s"Started RxIrcBot on ${socket.getInetAddress()}")
    println(s"Press any key to kill process")

    val input = Observable((subs: Subscriber[String]) => {
      val reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
      try {
        while (true) {
          val cmd = reader.readLine
          if (cmd == null) throw new IllegalStateException("Read null")
          subs.onNext(cmd)
        }
      } catch {
        case e: Exception =>
          subs.onError(e)
      }
    }).observeOn(IOScheduler()).publish

    new RxIrcBot(new OutputStreamWriter(socket.getOutputStream()), input).init()
    StdIn.readLine()
    socket.close()
  }
}

class RxIrcBot(out: OutputStreamWriter, in: ConnectableObservable[String]) {
  import RxIrcBot._

  def init(): Unit = {
    sendCmd("NICK", nick)
    sendCmd("USER", s"$nick 0 * : $real")
    sendCmd("JOIN", chan)

    sendMsg("Challo")

    // Log all messages
    if (logMessages) in.subscribe(cmd => println("< " + cmd), exc => println(exc))
    
    // Play PING -> PONG (keep alive)
    in.filter(isPing).subscribe(cmd => sendPong(cmd))

    // Handle messages
    in.filter(isPrivMsg).subscribe(
      msg => {
        val nick = senderNick(msg)
        senderMsg(msg) match {
          case "BANG" => sendMsg("💥💥💥💥")
          case s: String => sendMsg(s"Shut up " + nick)
        }
      }
    )

    // Connect multiple Observers to one HOT Observable. This is to ensure that all Observers 
    // get the events from the same single Observable reading from the InputStream.
    in.connect
  }

  /** Encrypts the argument using rot13. */
  def rot13(s: String): String = s map {
    case c if 'a' <= c.toLower && c.toLower <= 'm' => c + 13 toChar
    case c if 'n' <= c.toLower && c.toLower <= 'z' => c - 13 toChar
    case c => c
  }

  /** Tests whether a command is a user sent message and not a system message. */
  def isPrivMsg(cmd: String): Boolean =
    cmd.dropWhile(c => c != ' ').drop(1).takeWhile(c => c != ' ') == "PRIVMSG"

  /** Extracts the message sent by a user. */
  def senderMsg(cmd: String): String =
    cmd.drop(1).dropWhile(c => c != ':').drop(1)

  /** Extracts the nick name of the sender. */
  def senderNick(cmd: String): String =
    cmd.drop(1).takeWhile(c => c != '!')

  /** Tests whether it is a PING watchdog message. */
  def isPing(cmd: String): Boolean =
    cmd.startsWith("PING :")

  /** Sends a PONG message to the server. */
  def sendPong(cmd: String): Unit =
    sendCmd("PONG", ":" + cmd.drop(6))

  /** Send a message to the channel. */
  def sendMsg(msg: String): Unit =
    sendCmd("PRIVMSG", s"$chan : $msg")

  /** Send an IRC message to the server. */
  def sendCmd(action: String, args: String): Unit = {
    if (logMessages) println(s"> $action $args")
    out.write(s"$action $args\r\n")
    out.flush()
  }
}