package thisescapesjfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ThisEscape1 {
   public final int i;

   public ThisEscape1(Button btn) {
      btn.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
            ThisEscape1.this.doSomething(event);
         }
      });
      i = 42;
   }

   protected void doSomething(ActionEvent event) {
      System.out.println(i);
   }
}
