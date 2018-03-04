package worksheet;

public class Threadception {
	public static void main(String[] args) throws Exception {
		System.out.println("#CPUs: " + Runtime.getRuntime().availableProcessors());
		for (int i = 0; i < 1000000000; i++) {
			Thread t1 = new Thread(new Run(i));
			t1.start();

		}
		System.out.println("main: done");
	}
}

class Run implements Runnable {

	public Run(int p) {
        System.out.println("Thread no " + p + " started");
	}

	public void run() {
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}