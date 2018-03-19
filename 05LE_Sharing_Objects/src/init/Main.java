package init;

public class Main {

	public static void main(String[] args) {
		Thread t1 = new Thread(() -> new A());
		t1.start();
		Thread t2 = new Thread(() -> new B());
		t2.start();
	}

}

class A {
	static int x = 1;
	static {
		System.out.println(Thread.currentThread());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(B.x);
	}
}

class B {
	static int x = 1;
	static {
		System.out.println(Thread.currentThread());
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(A.x);
	}
}

// http://kohsuke.org/2010/09/01/deadlock-that-you-cant-avoid/	