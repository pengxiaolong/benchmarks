package personal.xlpeng.counters.jcstress;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.L_Result;
import personal.xlpeng.counters.Counter;
import personal.xlpeng.counters.VarHandleCounter;

import static org.openjdk.jcstress.annotations.Expect.*;

@JCStressTest
@Description("Test VarHandleCounter is thread-safe")
// These are the test outcomes.
@Outcome(id = "1", expect = FORBIDDEN,  desc = "Atomicity failure.")
@Outcome(id = "2", expect = ACCEPTABLE, desc = "Actors updated independently.")
@State
public class VarHandleCounterJCStressTest {
    private final Counter counter = new VarHandleCounter();

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
