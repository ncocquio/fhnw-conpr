package atomics;
import sun.misc.Unsafe;

//Required VM Arguments: -Xbootclasspath/p:${project_loc:/06_LE_Lock_Free}/bin/.

public class UnsafeCounter {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;
    private volatile long value = 0;
    
    static {
        try {
            valueOffset = unsafe.objectFieldOffset(
            		UnsafeCounter.class.getDeclaredField("value"));
        } catch (Exception ex) { 
            throw new Error(ex);
        }
    }

    public long getValue() {
        return value;
    }

    public long increment() {
        while (true) {
            long current = getValue();
            long next = current + 1;
            if (compareAndSwap(current, next))
                return next;
        }
    }

    private boolean compareAndSwap(long expected, long newVal) {
        return unsafe.compareAndSwapLong(this, valueOffset, expected, newVal);
    }
    
    public static void main(String[] args) {
    	final UnsafeCounter c = new UnsafeCounter();
    	
    	for(int i = 0; i < 100; i++) {
    		c.increment();
    	}
    	System.out.println(c.value);
	}
}