package set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class ITreeSetTest {
   
   private static <E extends Comparable<E>> void assertContains(ITreeSet<E> set, E e) {
      assertTrue(set.contains(e));
   }
   
   private static <E extends  Comparable<E>> void assertNotContains(ITreeSet<E> set, E e) {
      assertFalse(set.contains(e));
   } 
   
   public ITreeSet<Integer> EMPTY = TreeSet.<Integer>empty();
   
   @Test 
   public void setInsertion() {
      ITreeSet<Integer> s0 = EMPTY;
      ITreeSet<Integer> s1 = s0.insert(5);
      ITreeSet<Integer> s2 = s1.insert(3);
      ITreeSet<Integer> s3 = s2.insert(6);
      
      assertNotContains(s0, 5);
      assertNotContains(s0, 3);
      assertNotContains(s0, 6);
      
      assertContains(s1, 5);
      assertNotContains(s1, 3);
      assertNotContains(s1, 6);
      
      assertContains(s2, 5);
      assertContains(s2, 3);
      assertNotContains(s2, 6);
      
      assertContains(s3, 5);
      assertContains(s3, 3);
      assertContains(s3, 6);
   }
   
   @Test 
   public void setInsertion2() {
      ITreeSet<Integer> s0 = EMPTY;
      ITreeSet<Integer> s100 = s0.insert(100);
      assertContains(s100, 100);
      ITreeSet<Integer> s1000 = s0.insert(1000);
      assertContains(s1000, 1000);
   } 

}
