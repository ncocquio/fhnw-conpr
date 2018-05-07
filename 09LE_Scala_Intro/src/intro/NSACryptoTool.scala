package intro

import javax.swing.JFrame
import javax.swing.JTextArea
import java.awt.FlowLayout
import javax.swing.event.DocumentListener
import javax.swing.event.DocumentEvent
import scala.language.implicitConversions
/* Oder aber Sie verwenden http://www.rot13.com/ */
object NSACryptoTool {
  def main(args: Array[String]): Unit = {
    val f = new JFrame("NSA-Proof-Crypto-Technology")
    val fld1 = new JTextArea(40, 40)
    val fld2 = new JTextArea(40, 40)

    fld1.setLineWrap(true)
    fld2.setLineWrap(true)
    fld2.setEditable(false)
    fld1.getDocument().addDocumentListener(new DocumentListener() {
      def update() = fld2.setText(rot13(fld1.getText()))
      def removeUpdate(e: DocumentEvent) = update()
      def insertUpdate(e: DocumentEvent) = update()
      def changedUpdate(e: DocumentEvent) = update()
    })
    
    f.setLayout(new FlowLayout())
    f.add(fld1)
    f.add(fld2)
    f.pack()
    f.setVisible(true)
  }

  def rot13(input: String): String =
    input.map { c =>
      if (c >= 'a' && c <= 'm') c + 13
      else if (c >= 'A' && c <= 'M') c + 13
      else if (c >= 'n' && c <= 'z') c - 13
      else if (c >= 'N' && c <= 'Z') c - 13
      else c
    }.map(_.toChar).mkString
}