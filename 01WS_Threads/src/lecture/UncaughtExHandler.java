package lecture;

public class UncaughtExHandler {
	public static void main(String[] args) {
		Thread t = new Thread() {
			public void run() {
				throw new IllegalStateException("Test");
			}
		};
		
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("Thread " + t + " died because of " + e);
				System.out.println(t == Thread.currentThread());
			}
		});
		
		t.start();
	}
}
