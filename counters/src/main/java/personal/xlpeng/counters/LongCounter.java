package personal.xlpeng.counters;

public class LongCounter implements Counter {
    private long count = 0;

    @Override
    public void inc() {
        count ++;
    }

    @Override
    public long get() {
        return count;
    }
}
