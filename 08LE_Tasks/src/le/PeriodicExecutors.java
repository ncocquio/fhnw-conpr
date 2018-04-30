package le;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PeriodicExecutors {
	public static void main(String[] args) {
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
		ScheduledFuture<?> future = ses.scheduleAtFixedRate(() -> System.out.println(System.currentTimeMillis()), 0, 1,
				TimeUnit.SECONDS);

		ses.schedule(() -> {
			future.cancel(true);
			ses.shutdown();
		}, 10, TimeUnit.SECONDS);
	}
}
