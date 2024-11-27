package personal.xlpeng.counters;

import java.util.concurrent.atomic.LongAdder;

public class LongAdderCounter implements Counter {
    private final LongAdder adder = new LongAdder();
    @Override
    public void inc() {
        adder.increment();
    }

    @Override
    public long get() {
        return adder.sum();
    }
}
