import java.util

import akka.actor.FSM.->
import akka.actor._

import scala.collection.immutable.HashMap

object Actor_chat {
  val as = ActorSystem("as")

  case class JoinRoomRequest(name: String)
  case class JoinChatRoom(user: ActorRef)
  case class JoinRoom(name: String, server: ActorRef)
  case class JoinedRoomMsg()
  case class ListRooms(server: ActorRef)
  case class ListRoomRequest()
  case class SendMsg(msg: String)
  case class SendRoomMsg(name: String, msg: String)


  class ChatServer extends Actor {
    var roomList: Map[String, ActorRef] = Map()

    override def receive: Receive = {
      case JoinRoomRequest(name) => {
        var newRoom: ActorRef = null
        roomList.get(name) match {
          case Some(room) => newRoom = room
          case None => newRoom = as.actorOf(Props(new ChatRoom(name)))
        }
        newRoom ! JoinChatRoom(sender)
        roomList += (name -> newRoom)
      }
      case ListRoomRequest => {
        var textRoomList: String = "Room List: "
        for(r <- roomList) {
          textRoomList += r._1 + ", "
        }
        sender ! textRoomList
      }
    }
  }

  class ChatRoom(name: String) extends Actor {
    var userList: List[ActorRef] = List()

    override def receive: Receive = {
      case JoinChatRoom(user) => {
        userList = user :: userList
        user ! s"Welcome to " + name
        SendRoomMsg(name, "User joined")
        user ! JoinedRoomMsg
      }
      case SendRoomMsg(name, msg) => userList.foreach(u => u ! name + ": " + msg)
    }
  }

  class ChatUser(name: String) extends Actor {
    var currentRoom: ActorRef = null

    override def receive: Receive = {
      case JoinRoom(name, server) => server ! JoinRoomRequest(name)
      case JoinedRoomMsg => currentRoom = sender
      case ListRooms(server) => server ! ListRoomRequest
      case SendMsg(msg) if currentRoom != null => currentRoom ! SendRoomMsg(name, msg)
      case msg => println("<" + name + " recevied message: " + msg + ">")
    }
  }

  def main(args: Array[String]): Unit = {
    val user1 = as.actorOf(Props(new ChatUser("user1")))
    val user2 = as.actorOf(Props(new ChatUser("user2")))
    val user3 = as.actorOf(Props(new ChatUser("user3")))

    val chatServer = as.actorOf(Props[ChatServer])

    user1 ! JoinRoom("News", chatServer)
    user1 ! JoinRoom("Animals", chatServer)
    user1 ! JoinRoom("Weather", chatServer)
    user2 ! ListRooms(chatServer)
    user2 ! JoinRoom("Weather", chatServer)
    user3 ! ListRooms(chatServer)
    user3 ! JoinRoom("Weather", chatServer)
    Thread.sleep(1000)
    user3 ! SendMsg("Hallo")
  }
}