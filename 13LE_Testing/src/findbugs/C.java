package findbugs;

public class C {
  public int cnt;
  
  private static C instance;
  
  public static C instance() {
    if(instance == null) {
      synchronized (C.class) {
        if(instance == null) {
          instance = new C();
        }
      }
    }
    return instance;
  }
}
