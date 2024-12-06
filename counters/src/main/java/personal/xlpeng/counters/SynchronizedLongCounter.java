package personal.xlpeng.counters;

public class SynchronizedLongCounter implements Counter {
    private long count = 0;

    @Override
    public synchronized void inc() {
        count ++;
    }

    @Override
    public synchronized long get() {
        return count;
    }
}
