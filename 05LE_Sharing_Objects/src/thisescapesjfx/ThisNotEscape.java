package thisescapesjfx;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ThisNotEscape {
    public final int i;
    private ThisNotEscape() { i = 42; }
    
    public static ThisNotEscape create(Button btn) {
        final ThisNotEscape notEscape = new ThisNotEscape();
        btn.setOnAction((event) -> notEscape.doSomething(event));
        return notEscape;
    }

    protected void doSomething(ActionEvent e) {
        System.out.println(i);
    }
}
