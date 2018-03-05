package interrupt;

import java.util.concurrent.locks.ReentrantLock;

public class UnlockTest {

   public static void main(String[] args) throws InterruptedException {

      Thread good = new Thread() {
         public void run() {
            try {
               interrupt();
               ReentrantLock lock = new ReentrantLock();

               lock.lockInterruptibly();
               try {
                  // do locked
               } finally {
                  lock.unlock();
               }
            } catch (InterruptedException ex) {
               System.out.println("InterruptedException");
            }

         }
      };

      good.start();
      good.join();

      System.out.println("Checkpoint!");

      Thread bad = new Thread() {
         public void run() { 
            ReentrantLock lock = new ReentrantLock();
            interrupt();
            try {
               lock.lockInterruptibly();
               // do locked
            } catch (InterruptedException ex) {
               System.out.println("InterruptedException");
            } finally {
               lock.unlock();
            }
         }
      };
      bad.start();
   }
}
