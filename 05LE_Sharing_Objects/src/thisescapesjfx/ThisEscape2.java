package thisescapesjfx;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ThisEscape2 {
   public final int i;

   public ThisEscape2(Button btn) {
      btn.setOnAction((event) -> doSomething(event));
      i = 42;
   }

   protected void doSomething(ActionEvent event) {
      System.out.println(i);
   }
}
