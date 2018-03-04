package lecture;

import java.io.PrintStream;

public class Test1 {

  public static void main(String[] args) throws Exception {
    Thread t1 = new Thread(new R(System.out), "1");
    Thread t2 = new Thread(new R(System.err), "2");
    t2.setDaemon(true);
    t1.start();
    t2.start();
    
	
    //System.out.println("done");

    //t1.join();
    //t2.join();

    // t1.setDaemon(true);
    // t2.setDaemon(true);

    // System.out.println(Thread.currentThread().getName());
  }
}

class R implements Runnable {
  private PrintStream p;

  public R(PrintStream p) {
    this.p = p;
  }

  public void run() {
    for (int i = 0; i < 10; i++) {
      p.println("Hello" + Thread.currentThread().getName() + " " + i);
    }
  }
}

/*
 * Try Thread.currentThread().yield(); or try { Thread.sleep(0,1); } catch
 * (InterruptedException e) {} inside the loop
 */
