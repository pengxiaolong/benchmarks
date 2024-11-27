package personal.xlpeng.counters;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongCounter implements Counter {
    private final AtomicLong count = new AtomicLong(0);
    @Override
    public void inc() {
        count.incrementAndGet();
    }

    @Override
    public long get() {
        return count.get();
    }
}
