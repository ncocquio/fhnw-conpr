package observables

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.WindowConstants
import rx.lang.scala.JavaConversions
import rx.lang.scala.Observable
import rx.lang.scala.Observer
import rx.lang.scala.Subscription
import rx.observables.SwingObservable

sealed trait Figure
case class Line(x1: Int, y1: Int, x2: Int, y2: Int) extends Figure
case class Circle(x: Int, y: Int, r: Int) extends Figure

class RxFrame extends JFrame {

  val drawPad = new RxDrawPad

  def run = {
    initLayout

    val mouseMoveEvents: Observable[MouseEvent] = 
      JavaConversions.toScalaObservable(SwingObservable.fromMouseMotionEvents(drawPad))
      
    val mousePos: Observable[(Int, Int)] = 
      mouseMoveEvents.map(me => (me.getX(), me.getY()))

    val lines: Observable[Line] = 
      mousePos.slidingBuffer(2, 1).map{ 
        case Seq((x1, y1), (x2,y2)) => Line(x1, y1, x2, y2)
      }
      //.filter(l => l.x1 > 200)
      //.flatMap(l => Observable.items(l, Line(l.x1 + 10, l.y1+ 10, l.x2 + 10, l.y2 + 10)))
    
    val circles: Observable[Circle] = {
      def size(y: Int): Int = Math.max(((y / 400d) * 30).toInt, 5)
      mousePos.map{ case (x, y) => Circle(x, y, size(y)) }
    }
      
    val figures = lines //.merge(circles)
      
    // emits true when mouse button 1 goes down, false when it goes up
    val mouseDown: Observable[Boolean] = 
      MouseEventSource.mouseButtonDown(drawPad, MouseEvent.BUTTON1)

    // Paint if the mouse is down
    val paint: Observable[Figure] =
      mouseDown.map(down => if (down) figures else Observable.empty).switch

    paint.subscribe(f => drawPad.drawFigure(f))
  }

  def initLayout = {
    add(drawPad)
    setSize(400, 400)
    setResizable(false)
    setTitle("RxScala Paint")
    setVisible(true)
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  }
}

class RxDrawPad extends JComponent {

  var image: Image = null
  var graphics2D: Graphics2D = null

  setDoubleBuffered(false)

  override def paintComponent(g: Graphics) {
    if (image == null) {
      image = createImage(getSize().width, getSize().height)
      graphics2D = image.getGraphics().asInstanceOf[Graphics2D]
      graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      clear()
    }
    g.drawImage(image, 0, 0, null)
  }

  def drawFigure(l: Figure): Unit = {
    l match {
      case Line(x1, y1, x2, y2) => graphics2D.drawLine(x1, y1, x2, y2)
      case Circle(x, y, r) => graphics2D.drawOval(x - r/2, y - r/2, r, r)
    }
    repaint()
  }

  def clear() {
    graphics2D.setPaint(Color.white)
    graphics2D.fillRect(0, 0, getSize().width, getSize().height)
    graphics2D.setPaint(Color.black)
    repaint()
  }
}

object MouseEventSource {

  /**
   * @returns an Observable that emits true when mouse button {@code buttonId} goes down and
   *          false when it goes up
   */
  def mouseButtonDown(comp: JComponent, buttonId: Int): Observable[Boolean] = Observable.create(
    (observer: Observer[Boolean]) => {
      val listener = new MouseAdapter() {
        override def mousePressed(e: MouseEvent): Unit = {
          if (e.getButton() == buttonId) observer.onNext(true)
        }
        override def mouseReleased(e: MouseEvent): Unit = {
          if (e.getButton() == buttonId) observer.onNext(false)
        }
      }
      comp.addMouseListener(listener)
      new Subscription {
        override def unsubscribe: Unit = comp.removeMouseListener(listener)
      }
    })
}

object ShinyNewPaint extends App {
  new RxFrame().run
}