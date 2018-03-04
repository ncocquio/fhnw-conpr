package as;

import javafx.application.Platform;
import javafx.beans.value.WritableStringValue;

import java.util.LinkedList;
import java.util.List;

public class MandelbrotProgress implements Runnable {
    private PixelPainter painter;
    private Plane plane;
    private WritableStringValue millis;
    private CancelSupport cancelSupport;
    private int noThreads;

    public MandelbrotProgress(PixelPainter painter, Plane plane, WritableStringValue millis, CancelSupport cancelSupport, int noThreads) {
        this.painter = painter;
        this.plane = plane;
        this.millis = millis;
        this.cancelSupport = cancelSupport;
        this.noThreads = noThreads;
    }

    @Override
    public void run() {
        double start = System.currentTimeMillis();
        // Replace the following line with Mandelbrot.computeParallel(...)
        //Mandelbrot.computeSequential(painter, plane, cancelSupport);
        Mandelbrot.computeParallel(painter, plane, cancelSupport, noThreads);

        double end = System.currentTimeMillis();
        Platform.runLater(() -> millis.set((end - start) + "ms"));
    }
}
