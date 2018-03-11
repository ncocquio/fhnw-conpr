package semaphore;

import java.util.concurrent.ConcurrentLinkedQueue;

public final class SemaphoreImpl implements Semaphore {
    private ConcurrentLinkedQueue<Thread> fifoQueue = new ConcurrentLinkedQueue<>();
    private int value;

    public SemaphoreImpl(int initial) {
        if (initial < 0) throw new IllegalArgumentException();
        value = initial;
    }

    @Override
    public int available() {
        return value;
    }

    @Override
    public synchronized void acquire() {
        fifoQueue.add(Thread.currentThread());
        while (value == 0 || !Thread.currentThread().equals(fifoQueue.peek())) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        fifoQueue.poll();
        value--;
        notifyAll();
    }

    @Override
    public synchronized void release() {
        value++;
        notifyAll();
    }
}
