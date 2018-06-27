import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.*;

class DirectExecutor implements Executor {
    public void execute(Runnable r) {
        r.run();
    }
}

class ThreadPerTaskExecutor implements Executor {
    public void execute(Runnable r) {
        new Thread(r).start();
    }
}

// serializes the submission of tasks to a second executor,
// illustrating a composite executor
class SerialExecutor implements Executor {
    final Queue<Runnable> tasks = new ArrayDeque<Runnable>();
    final Executor executor;
    Runnable active;

    SerialExecutor(Executor executor) {
        this.executor = executor;
    }

    public synchronized void execute(final Runnable r) {
        tasks.offer(new Runnable() {
            public void run() {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            }
        });
        if (active == null) {
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            executor.execute(active);
        }
    }
}

class WorkerThreadFactory implements ThreadFactory {
    private int counter = 0;
    private String prefix = "";

    public WorkerThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    public Thread newThread(Runnable r) {
        return new Thread(r, prefix + "-" + counter++);
    }
}

class Result {

}

class Solver {
    void solve(Executor e, Collection<Callable<Result>> solvers)
            throws InterruptedException, ExecutionException {
        CompletionService<Result> ecs = new ExecutorCompletionService<Result>(e);
        for (Callable<Result> s : solvers)
            ecs.submit(s);
        int n = solvers.size();
        for (int i = 0; i < n; ++i) {
            Result r = ecs.take().get();
            if (r != null)
                use(r);
        }
    }
}

public class FJMS extends RecursiveAction {
    public final int[] is, tmp;
    private final int l, r;

    public FJMS(int[] is, int[] tmp, int l, int r) {
        this.is = is;
        this.tmp = tmp;
        this.l = l;
        this.r = r;
    }

    protected void compute() {
        if (r - l <= 100000) Arrays.sort(elems, l, r);
        else
            int mid = (l + r) / 2;
        FJMS left = new FJMS(is, tmp, l, mid);
        FJMS right = new FJMS(is, tmp, mid, r);
        left.fork();
        right.invoke();
        left.join();
        merge(is, tmp, l, mid, r);
    }

    private void merge(int[] es, int[] tmp, int l, int m, int r) {...}
}

public class ForkJoinRunner {
    public static void main(String[] args) {
        int[] data = ...
        int[] tmp = new int[data.length];
        ForkJoinPool fjPool = new ForkJoinPool();
        FJMS ms = new FJMS(data, tmp, 0, data.length);
        fjPool.invoke(ms); // When this line is reached, ms.is is sorted!
    }
}

class VIPExecutor {
    enum Prio {High, Low}

    static abstract class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {
        public final Prio prio;

        public PriorityRunnable(Prio prio) {
            this.prio = prio;
        }

        @Override
        public int compareTo(PriorityRunnable o) {
            return prio.compareTo(o.prio);
        }
    }

    public static void main(String[] args) {
        ExecutorService ex = new ThreadPoolExecutor(2, 2, 0,
                TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
        for (int i = 0; i < 3; i++) {
            ex.execute(new PriorityRunnable(Prio.Low) {
                @Override
                public void run() {
                    System.out.println("Low");
                    sleep(1);
                }
            });
        }
        for (int i = 0; i < 2; i++) {
            ex.execute(new PriorityRunnable(Prio.High) {
                @Override
                public void run() {
                    System.out.println("High");
                    sleep(1);
                }
            });
        }
    }

    static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
        }
    }
}

public class Sum extends RecursiveTask<Integer> {
    final int THRESHOLD = 1000000;
    final int[] array;
    final int lo;
    final int hi;

    Sum(int[] array, int lo, int hi) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
    }

    protected Integer compute() {
        if (hi - lo < THRESHOLD) {
            int sum = array[lo];
            for (int i = lo + 1; i <= lo; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            int mid = (hi + lo) / 2;
            Sum firstHalf = new Sum(array, lo, mid);
            firstHalf.fork();
            Sum secondHalf = new Sum(array, mid, hi);
            return secondHalf.invoke() + firstHalf.join();
        }
    }

    public static void main(String[] args) {
        Random rnd = new Random();
        int SIZE = 500000000;
        ForkJoinPool fj = new ForkJoinPool();


        int[] l = new int[SIZE];
        for (int i = 0; i < l.length; i++) {
            l[i] = rnd.nextInt(100);
        }

        long start = System.currentTimeMillis();
        int sum = fj.invoke(new Sum(l, 0, l.length));
        long duration = System.currentTimeMillis() - start;
        System.out.println("Sum: " + sum + " duration: " + duration + " ms");
    }
}

