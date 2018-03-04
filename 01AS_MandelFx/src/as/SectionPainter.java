package as;

import static as.Mandelbrot.getColor;
import static as.Mandelbrot.mandel;

public class SectionPainter implements Runnable {
    private PixelPainter painter;
    private CancelSupport cancel;

    private int endX;
    private double reMin;
    private double imMax;
    private double step;

    private int startX;

    public SectionPainter(PixelPainter painter, CancelSupport cancel, double reMin, double imMax, double step, int startX, int endX) {
        this.painter = painter;
        this.cancel = cancel;
        this.endX = endX;
        this.reMin = reMin;
        this.imMax = imMax;
        this.step = step;
        this.startX = startX;
    }

    @Override
    public void run() {
        for (int x = startX; x < endX && !cancel.isCancelled(); x++) { // x-axis
            double re = reMin + x * step; // map pixel to complex plane
            for (int y = 0; y < 1024; y++) { // y-axis
                double im = imMax - y * step; // map pixel to complex plane

                //int iterations = mandel(re, im);
                int iterations = mandel(new Complex(re, im));
                painter.paint(x, y, getColor(iterations));
            }
        }
    }
}
