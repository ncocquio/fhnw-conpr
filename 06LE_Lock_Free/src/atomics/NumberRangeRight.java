package atomics;

import java.util.concurrent.atomic.AtomicReference;

public class NumberRangeRight {

    static class NumberRange{
        public final int lower;
        public final int upper;

        NumberRange(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }
    }

    private final AtomicReference<NumberRange> numberRange = new AtomicReference<>(new NumberRange(0,0));

    private int getLower() {
        return numberRange.get().lower;
    }

    public void setLower(int i) {
        while (true) {
            NumberRange oldNumberRange = numberRange.get();
            if (i > oldNumberRange.upper)
                throw new IllegalArgumentException();
            if (numberRange.compareAndSet(oldNumberRange, new NumberRange(i,oldNumberRange.upper)))
                return;
        }
    }

    public int getUpper() {
        return numberRange.get().upper;
    }

    public void setUpper(int i) {
        while (true) {
            NumberRange oldNumberRange = numberRange.get();
            if (i < oldNumberRange.lower)
                throw new IllegalArgumentException();
            if (numberRange.compareAndSet(oldNumberRange, new NumberRange(oldNumberRange.lower,i)))
                return;
        }
    }

    public boolean contains(int i) {
        return getLower() <= i && i <= getUpper();
    }
}
