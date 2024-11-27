package personal.xlpeng.counters.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import personal.xlpeng.counters.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Threads(16)
@Fork(1)
@Warmup(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class CounterBenchmarkTest {
    final LongCounter longCounter = new LongCounter();
    final LongAdderCounter longAdderCounter = new LongAdderCounter();
    final AtomicLongCounter atomicLongCounter = new AtomicLongCounter();
    final AtomicLongFieldUpdaterCounter atomicLongFieldUpdaterCounter = new AtomicLongFieldUpdaterCounter();
    final VarHandleCounter varHandleCounter = new VarHandleCounter();
    final VolatileLongCounter volatileLongCounter= new VolatileLongCounter();

    @Benchmark
    public void longCounterInc() {
        longCounter.inc();
    }

    @Benchmark
    public void longCounterGet(Blackhole bh) {
        bh.consume(longCounter.get());
    }

    @Benchmark
    public void longAdderCounterInc() {
        longAdderCounter.inc();
    }

    @Benchmark
    public void longAdderAdderCounterGet(Blackhole bh) {
        bh.consume(longAdderCounter.get());
    }

    @Benchmark
    public void atomicLongCounterInc() {
        atomicLongCounter.inc();
    }

    @Benchmark
    public void atomicLongCounterGet(Blackhole bh) {
        bh.consume(atomicLongCounter.get());
    }

    @Benchmark
    public void atomicLongFieldUpdaterCounterInc() {
        atomicLongFieldUpdaterCounter.inc();
    }

    @Benchmark
    public void atomicLongFieldUpdaterCounterGet(Blackhole bh) {
        bh.consume(atomicLongFieldUpdaterCounter.get());
    }

    @Benchmark
    public void varHandleCounterInc() {
        varHandleCounter.inc();
    }

    @Benchmark
    public void varHandleCounterGet(Blackhole bh) {
        bh.consume(varHandleCounter.get());
    }

    @Benchmark
    public void volatileLongCounterInc() {
        volatileLongCounter.inc();
    }

    @Benchmark
    public void volatileLongCounterGet(Blackhole bh) {
        bh.consume(volatileLongCounter.get());
    }
}
