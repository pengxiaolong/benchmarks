package personal.xlpeng.counters;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class VarHandleCounter implements Counter {
    private long count = 0L;
    private final VarHandle COUNT;

    public VarHandleCounter() {
        try {
            COUNT = MethodHandles.lookup().findVarHandle(VarHandleCounter.class, "count", long.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void inc() {
        COUNT.getAndAdd(this, 1);
    }

    @Override
    public long get() {
        return (long)(COUNT.getOpaque(this));
    }
}
