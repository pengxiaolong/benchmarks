package personal.xlpeng.counters.jcstress;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.L_Result;
import personal.xlpeng.counters.AtomicLongCounter;
import personal.xlpeng.counters.AtomicLongFieldUpdaterCounter;
import personal.xlpeng.counters.Counter;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

@JCStressTest
@Description("Test AtomicLongFieldUpdaterCounter is thread-safe")
// These are the test outcomes.
@Outcome(id = "1", expect = FORBIDDEN,  desc = "Atomicity failure.")
@Outcome(id = "2", expect = ACCEPTABLE, desc = "Actors updated independently.")
@State
public class AtomicLongFieldUpdaterCounterJCStressTest {
    private final Counter counter = new AtomicLongFieldUpdaterCounter();

    @Actor
    public void actor1() {
        counter.inc();
    }

    @Actor
    public void actor2() {
        counter.inc();
    }

    @Arbiter
    public void arbiter(L_Result result) {
        result.r1 = counter.get();;
    }
}
