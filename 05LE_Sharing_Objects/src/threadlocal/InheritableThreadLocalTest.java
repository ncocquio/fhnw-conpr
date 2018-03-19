package threadlocal;

public class InheritableThreadLocalTest {

	static ThreadLocal<Integer> value = new InheritableThreadLocal<Integer>() {
		@Override
		public Integer initialValue() {
			return 0;
		}
	};

	public static void foo() {
		System.out.println(Thread.currentThread().getName() + ":  "
				+ value.get());
		value.set(1 + value.get());
	}

	static class T extends Thread {
		int n;

		T(int n) {
			this.n = n;
		}

		@Override
		public void run() {
			foo();
			for (int i = 0; i < n; i++) {
				T t = new T(n-1);
				t.start();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new T(2).start();
	}

}
