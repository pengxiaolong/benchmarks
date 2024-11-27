package personal.xlpeng.counters;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class AtomicLongFieldUpdaterCounter implements Counter {
    private volatile long count = 0L;
    private static final AtomicLongFieldUpdater<AtomicLongFieldUpdaterCounter> COUNT =
            AtomicLongFieldUpdater.newUpdater(AtomicLongFieldUpdaterCounter.class, "count");;
    @Override
    public void inc() {
        COUNT.incrementAndGet(this);
    }

    @Override
    public long get() {
        return COUNT.get(this);
    }
}
