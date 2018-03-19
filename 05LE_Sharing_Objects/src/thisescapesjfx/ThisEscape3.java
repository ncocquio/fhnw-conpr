package thisescapesjfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ThisEscape3 implements EventHandler<ActionEvent> {
   public final int i;

   public ThisEscape3(Button btn) {
      btn.setOnAction(this);
      i = 42;
   }

   @Override
   public void handle(ActionEvent event) {
      System.out.println(i);
   }
}
