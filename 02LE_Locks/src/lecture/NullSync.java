package lecture;

public class NullSync {
   public static void main(String[] args) {
      Object bla = new Object();
      synchronized (bla) {
         bla = null;
         System.out.println("BANG");
      }
   }
}
