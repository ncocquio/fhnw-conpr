package le;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecuterTermination {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {}
                System.out.println("Done");
            }
        });
        // Main does not terminate!
        
        exec.shutdown();
       
        
        exec.awaitTermination(1, TimeUnit.SECONDS);
    }
}
