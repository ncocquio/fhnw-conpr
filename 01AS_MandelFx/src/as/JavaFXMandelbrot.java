package as;

import static as.Mandelbrot.IMAGE_LENGTH;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.WritableDoubleValue;
import javafx.beans.value.WritableStringValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JavaFXMandelbrot extends Application {
  private static Plane INITIAL_PLANE = new Plane(new Complex(0, 0), 4);

  private final Property<Plane> plane = new SimpleObjectProperty<Plane>(new Plane(new Complex(0, 0), 0)); // Dummy
                                                                                                          // start
                                                                                                          // value

  private final StringProperty timeProp = new SimpleStringProperty();
  private final DoubleProperty progressProp = new SimpleDoubleProperty();
  private final BooleanProperty cancelProp = new SimpleBooleanProperty();

  private <A, B> Property<B> unimap(Property<A> aProp, Function<A, B> aToB) {
    Property<B> bProp = new SimpleObjectProperty<B>(aToB.apply(aProp.getValue()));
    aProp.addListener((o, oldVal, newVal) -> {
      bProp.setValue(aToB.apply(newVal));
    });

    return bProp;
  }

  @Override
  public void start(Stage stage) {
    BorderPane layout = new BorderPane();
    Scene scene = new Scene(layout, Color.BLACK);
    stage.setScene(scene);

    GridPane grid = createControlUI();
    layout.setRight(grid);

    final AnchorPane imageRoot = new AnchorPane();

    ImageView imageView = new ImageView();
    imageView.setFitHeight(IMAGE_LENGTH);
    imageView.setFitWidth(IMAGE_LENGTH);

    final AtomicReference<MouseEvent> start = new AtomicReference<>();
    final AtomicReference<Rectangle> rect = new AtomicReference<>();

    imageView.setOnMousePressed(e -> {
      Rectangle r = new Rectangle();
      r.setFill(null);
      r.setX(e.getX());
      r.setY(e.getY());
      r.setArcWidth(5);
      r.setArcHeight(5);
      r.setStroke(Color.WHITE);
      r.getStrokeDashArray().addAll(5d);
      r.setStrokeWidth(2);
      r.setBlendMode(BlendMode.DIFFERENCE);

      rect.set(r);
      start.set(e);
      imageRoot.getChildren().add(r);
    });

    imageView.setOnMouseDragged(e -> {
      MouseEvent s = start.get();
      double xStart = s.getX();
      double yStart = s.getY();
      double xEnd = e.getX();
      double yEnd = e.getY();

      double width = xEnd - xStart;
      double height = yEnd - yStart;
      double length = Math.max(Math.abs(width), Math.abs(height));

      double xRect = Math.min(xStart, xStart + (length * Math.signum(width)));
      double yRect = Math.min(yStart, yStart + (length * Math.signum(height)));

      Rectangle r = rect.get();
      r.setX(xRect);
      r.setY(yRect);
      r.setHeight(length);
      r.setWidth(length);
    });

    imageView.setOnMouseReleased(e -> {
      imageRoot.getChildren().remove(rect.get());
      plane.setValue(mouseToPlane(rect.get(), plane.getValue()));
      rect.set(null);
      start.set(null);
    });

    plane.addListener((o, oldPlane, newPlane) -> {
      Image image = drawMandel(imageView, newPlane, progressProp, cancelProp, timeProp);
      imageView.setImage(image);
    });
    imageRoot.getChildren().add(imageView);

    layout.setCenter(imageRoot);

    plane.setValue(INITIAL_PLANE);

    stage.setTitle("Mandelbrot Set");
    stage.show();

    System.out.println(Thread.getAllStackTraces().keySet());
  }

  private GridPane createControlUI() {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.TOP_CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    Text scenetitle = new Text("Control");
    scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(scenetitle, 0, 0, 2, 1);

    grid.add(new Label("re:"), 0, 1);
    TextField re = new TextField();
    re.textProperty().bind(unimap(plane, p -> "" + p.center.r));
    re.setEditable(false);
    grid.add(re, 1, 1);

    grid.add(new Label("im:"), 0, 2);
    TextField im = new TextField();
    im.textProperty().bind(unimap(plane, p -> "" + p.center.i));
    im.setEditable(false);
    grid.add(im, 1, 2);

    grid.add(new Label("Zoom:"), 0, 3);
    TextField zoom = new TextField();
    zoom.textProperty().bind(unimap(plane, p -> "" + p.length));
    zoom.setEditable(false);
    grid.add(zoom, 1, 3);

    grid.add(new Label("Progress:"), 0, 4);
    ProgressBar pb = new ProgressBar();
    pb.setMaxWidth(Double.MAX_VALUE);
    pb.progressProperty().bind(progressProp);
    grid.add(pb, 1, 4);

    grid.add(new Label("Time:"), 0, 5);
    TextField time = new TextField();
    time.textProperty().bind(timeProp);
    time.setEditable(false);
    grid.add(time, 1, 5);

    Button stopBtn = new Button("Stop");
    cancelProp.bind(stopBtn.pressedProperty());
    stopBtn.disableProperty().bind(progressProp.lessThan(1.0).not());
    grid.add(stopBtn, 0, 6);

    Button resetBtn = new Button("Reset");
    resetBtn.setOnAction(e -> plane.setValue(INITIAL_PLANE));
    grid.add(resetBtn, 1, 6);
    return grid;
  }

  private Plane mouseToPlane(Rectangle r, Plane p) {
    Complex old = p.center;

    double step = p.length / IMAGE_LENGTH;
    double reMin = old.r - p.length / 2;
    double imMax = old.i + p.length / 2;

    double re = reMin + (r.getX() + r.getWidth() / 2) * step;
    double im = imMax - (r.getY() + r.getHeight() / 2) * step;
    return new Plane(new Complex(re, im), r.getHeight() * step);
  }

  private Image drawMandel(ImageView imageView, Plane plane, WritableDoubleValue progress,
      ObservableBooleanValue cancelled, WritableStringValue millis) {

    millis.set("...");
    WritableImage image = new WritableImage(IMAGE_LENGTH, IMAGE_LENGTH);
    PixelPainter painter = new PixelWriterPixelPainter(image.getPixelWriter(), IMAGE_LENGTH * IMAGE_LENGTH, progress);

    CancelSupport cancelSupport = new CancelSupport();
    cancelled.addListener((o, oldVal, newVal) -> cancelSupport.cancel());

    Runnable r = new MandelbrotProgress(painter, plane, millis, cancelSupport, 8);
    Thread t = new Thread(r);
    t.start();

    return image;
  }

  public static void main(String[] args) {
    launch(args);
  }
};
