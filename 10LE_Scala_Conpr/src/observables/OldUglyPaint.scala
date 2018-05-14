package observables

/* Strongly adapted from https://github.com/samuelgruetter/rx-playground/blob/master/RxScalaPaint/src/main/scala/Paint.scala */

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.RenderingHints
import java.awt.event.MouseEvent
import javax.swing.JComponent
import javax.swing.WindowConstants
import javax.swing.JFrame
import java.awt.event.MouseMotionAdapter
import java.awt.event.MouseAdapter

class OldUglyFrame extends JFrame {
  val drawPad = new OldUglyDrawPad

  def run = {
    initLayout

    var lastEvent: MouseEvent = null

    drawPad.addMouseMotionListener(new MouseMotionAdapter() {
      override def mouseDragged(e: MouseEvent): Unit = {
        if (e.getButton() == MouseEvent.BUTTON1) {
          if (lastEvent != null) {
            drawPad.drawLine(lastEvent.getX(), lastEvent.getY(), e.getX(), e.getY())
          }
          lastEvent = e
        }
      }
    })

    drawPad.addMouseListener(new MouseAdapter() {
      override def mouseReleased(e: MouseEvent): Unit = {
        if (e.getButton() == MouseEvent.BUTTON1) lastEvent = null
      }
    })
  }

  def initLayout = {
    add(drawPad)
    setSize(400, 400)
    setResizable(false)
    setTitle("MouseListener Paint")
    setVisible(true)
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  }

}

class OldUglyDrawPad extends JComponent {

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

  def drawLine(x1: Int, y1: Int, x2: Int, y2: Int) {
    graphics2D.drawLine(x1, y1, x2, y2)
    repaint()
  }

  def clear() {
    graphics2D.setPaint(Color.white)
    graphics2D.fillRect(0, 0, getSize().width, getSize().height)
    graphics2D.setPaint(Color.black)
    repaint()
  }
}

object OldUglyPaint extends App {
  new OldUglyFrame().run
}
