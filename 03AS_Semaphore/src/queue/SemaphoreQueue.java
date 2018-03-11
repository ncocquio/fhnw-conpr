package queue;

import semaphore.Semaphore;
import semaphore.SemaphoreImpl;

public class SemaphoreQueue {
    private int size;
    private Semaphore free;
    private Semaphore used = new SemaphoreImpl(0);
    private String buf[];
    private int head = 0;
    private int tail = 0;

    public SemaphoreQueue(int size) {
        this.size = size;
        free = new SemaphoreImpl(size);
        buf = new String[size];
    }

    public String dequeue() {
        used.acquire();
        String result;
        int predictedHead = (head + free.available()) % size;
        result = buf[predictedHead];
        buf[predictedHead] = null;
        head = (predictedHead + 1) % size;
        free.release();
        return result;
    }

    public void enqueue(String x) {
        free.acquire();
        int predictedTail = (tail + used.available()) % size;
        buf[predictedTail] = x + "," + tail + "," + predictedTail;
        tail = (predictedTail + 1) % size;
        used.release();
    }

    public String[] getBuf() {
        return buf;
    }
}
