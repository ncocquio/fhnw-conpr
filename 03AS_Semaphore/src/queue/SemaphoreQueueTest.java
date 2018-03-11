package queue;

public class SemaphoreQueueTest {

    public static void main(String[] args) throws InterruptedException {
        goNuts();
    }

    static SemaphoreQueue newSemaphoreQueue(int size) {
        return new SemaphoreQueue(size);
    }

    static void goNuts() throws InterruptedException {
        final SemaphoreQueue s = newSemaphoreQueue(4);

        Thread t1 = run("T1", new Runnable() {
            public void run() {
                s.enqueue("T1");
            }
        });

        Thread t2 = run("T2", new Runnable() {
            public void run() {
                s.enqueue("T2");
            }
        });

        Thread t3 = run("T3", new Runnable() {
            public void run() {
                s.enqueue("T3");
            }
        });

        Thread t4 = run("T4", new Runnable() {
            public void run() {
                s.enqueue("T4");
            }
        });

        Thread t5 = run("T5", new Runnable() {
            public void run() {
                s.enqueue("T5");
            }
        });

        Thread t6 = run("T6", new Runnable() {
            public void run() {
                Object lol = s.dequeue();
            }
        });

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();

        for (String o : s.getBuf()) {
            if (o == null) {
                System.out.println("null");
            } else {
                System.out.println(o);
            }
        }
    }

    static Thread run(String name, Runnable r) {
        Thread t = new Thread(r, name);
        t.setDaemon(true);
        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.err.println(t.getName() + " failed: " + e);
            }
        });
        t.start();
        return t;
    }
}