package le;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class BadFormatter {
	private final SimpleDateFormat sdf 
	                      = new SimpleDateFormat();
	
	public String format(Date d) {
		return sdf.format(d);
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws InterruptedException {
      final Date d1 = new Date(1, 0, 1, 1, 1);
      final Date d2 = new Date(2, 1, 2, 2, 2);
      // Try GoodFormatter
      final BadFormatter bf = new BadFormatter();
      
      System.out.println("Correct formats");
      System.out.println(bf.format(d1));
      System.out.println(bf.format(d2));
      
      final Set<String> correctDates = new HashSet<>();
      correctDates.add(bf.format(d1)); 
      correctDates.add(bf.format(d2));
      final AtomicReference<String> r1 = new AtomicReference<String>();
      final AtomicReference<String> r2 = new AtomicReference<String>();
      while(true) {
         
         final Thread t1 = new Thread(() -> r1.set(bf.format(d1)));
         final Thread t2 = new Thread(() -> r2.set(bf.format(d2)));
         t1.start(); t2.start();
         t1.join(); t2.join();
         
         if(!correctDates.contains(r1.get())) {
            System.out.println("Bad result: " + r1.get());
            correctDates.add(r1.get());
         } else if(!correctDates.contains(r2.get())) {
            System.out.println("Bad result: " + r2.get());
            correctDates.add(r2.get());
         }
      }
   }
}

class GoodFormatter {
	public String format(Date d) {
		return new SimpleDateFormat().format(d);
	}
}

class ThreadLocalFormatter {
	private final ThreadLocal<SimpleDateFormat> local = 
	    ThreadLocal.withInitial(() -> new SimpleDateFormat());
	
	public String format(Date d) {
		return local.get().format(d);
	}
}


