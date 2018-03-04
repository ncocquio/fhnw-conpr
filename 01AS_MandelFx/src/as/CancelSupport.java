package as;
public class CancelSupport {
  private volatile boolean cancel;
  
  public boolean isCancelled() {
    return cancel;
  }
  
  public void cancel() {
    cancel = true;
  }
}