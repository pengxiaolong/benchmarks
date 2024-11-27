package personal.xlpeng.counters;

public class VolatileLongCounter implements Counter {
    private volatile long count = 0;

    @Override
    public void inc() {
        count++; // Not actually atomic under multi-threads
    }

    @Override
    public long get() {
        return count;
    }
}
