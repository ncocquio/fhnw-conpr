package threadlocal.random;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

// the test generates 100'000000 random numbers using java.util.Random
// and using ThreadLocalRandom.
public class RandomTest {
	
	interface RandomFactory {
		Random getRandom();
	}

	static long testRandomGenerator(final Supplier<Random> fact, final int n)
			throws InterruptedException {
		final CountDownLatch prepare = new CountDownLatch(n);
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch end = new CountDownLatch(n);
		
		for (int i = 0; i < n; i++) {
			new Thread() {
				public void run() {
					Random r = fact.get();
					for (int i = 0; i < 10000; i++) { // warmup
						r.nextInt(10000);
					}
					prepare.countDown();
					try {
						start.await();
					} catch (InterruptedException e) {
					}
					int upper = 100_000_000 / n;
					for (int i = 0; i < upper; i++) {
						r.nextInt(10000);
					}
					end.countDown();
				}
			}.start();
		}
		prepare.await();
		long time = System.currentTimeMillis();
		start.countDown();
		end.await();
		return System.currentTimeMillis() - time;
	}

	public static void main(String[] args) throws Exception {
		int n = 1;
		while (n <= 2000) {
			System.out.format("%d Threads: Random   %s msec\n", n,
					testRandomGenerator(() -> new Random(), n));
			System.out.format("%d Threads: ThreadLocalRandom %s msec\n", n,
					testRandomGenerator(() -> ThreadLocalRandom.current(), n));
			n = 2 * n;
		}
	}

}
