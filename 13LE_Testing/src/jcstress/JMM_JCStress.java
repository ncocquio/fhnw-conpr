package jcstress;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

import java.util.concurrent.atomic.AtomicInteger;

@JCStressTest
@Description("Tests racy assignments.")
@Outcome(id = "2, 2", expect = Expect.ACCEPTABLE, desc = "t1 first")
@Outcome(id = "1, 2", expect = Expect.ACCEPTABLE_INTERESTING, desc = "JMM Issue")
@Outcome(id = "1, 5", expect = Expect.ACCEPTABLE, desc = "t2 first")
@Outcome(id = "2, 5", expect = Expect.ACCEPTABLE, desc = "t1,t2")
@State
public class JMM_JCStress {
   private AtomicInteger ai = new AtomicInteger(5);
   private int i = 1;

    @Actor
    public void thread1() {
        i++;
        ai.set(i);
    }

    @Actor
    public void thread2(II_Result res) {
        int _ai = ai.get(); // (2)
        int _i = i;         // (1)

        res.r1 = _i;
        res.r2 = _ai;
    }
}
