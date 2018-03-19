package list;

public interface IList<E> {

  E head();
  IList<E> tail();
  boolean isEmpty();

  default IList<E> prepend(E e) {
    return new IList<E>() {
      public E head() { return e; }
      public IList<E> tail() { return IList.this; }
      public boolean isEmpty() { return false; }
    };
  }

  public static <E> IList<E> empty() {
    return new IList<E>() {
      public E head() { throw new IllegalStateException("head of Nil"); }
      public IList<E> tail() { throw new IllegalStateException("tail of Nil"); }
      public boolean isEmpty() { return true; }
    };
  }

}

class Main {
  public static void main(String[] args) {
    IList<Integer> l_e = IList.empty();
    IList<Integer> l_1e = l_e.prepend(1);
    IList<Integer> l_21e = l_1e.prepend(2);
    
    System.out.println(l_21e.tail() == l_1e);
    
    IList<Integer> ll_e = IList.empty();
    IList<Integer> ll_21e = ll_e.prepend(1).prepend(2);
    System.out.println(ll_21e.tail().head() == 1);
    
    IList<Integer> l = ll_21e;
    while(!l.isEmpty()) {
    	System.out.println(l.head());
    	l = l.tail();
    }
    
    Integer element = 3;
    Integer e = 5;
    System.out.println(element.compareTo(e));
  }
}
