package threadlocal;
public class ThreadLocalTest {

	static ThreadLocal<Integer> value = new ThreadLocal<Integer>(){
		@Override public Integer initialValue(){return 0;}
	};

	public static void localInc(){
		System.out.println(Thread.currentThread().getName() + ":  " + value.get());
		value.set(1 + value.get());
	}

	static class T extends Thread {
		int n;
		T(int n, String name) { super(name); this.n = n; }

		@Override
		public void run() {
			for (int i = 0; i < n; i++) { localInc(); }
		}
	}

	public static void main(String[] args) throws Exception {
		T t1 = new T(3, "T1"); t1.start();
		T t2 = new T(5, "T2"); t2.start();
		T t3 = new T(2, "T3"); t3.start();
		localInc();
		t1.join();
		t2.join();
		t3.join();
	}

}
