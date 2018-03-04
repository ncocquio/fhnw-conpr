package lecture;

public class ThreadAPI {
  public static void main(String[] args) throws InterruptedException {
    System.out.println(Thread.currentThread().getName());
    
    Thread other = new Thread(() -> {
      System.out.println(Thread.currentThread().getName());
      try {
        Thread.sleep(1000);
      } catch (Exception e) {}
    }, "Other");
    other.start();
    
    Thread.sleep(500);
    System.out.println(other.getState());
    
    System.out.println(Thread.currentThread().getState());
  }
}
