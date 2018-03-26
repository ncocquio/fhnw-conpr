package atomics;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

final class AtomicFieldUpdaterCounter {
	
	private static final AtomicLongFieldUpdater<AtomicFieldUpdaterCounter> updater 
	    = AtomicLongFieldUpdater.newUpdater(AtomicFieldUpdaterCounter.class, "value");

	private volatile long value;
	
	public long getValue() {
        return value;
    }

    public long increment() {
        return updater.incrementAndGet(this);
    }
}