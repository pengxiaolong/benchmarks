package personal.xlpeng.counters.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@Warmup(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SingleThreadMemoryAccessBenchmarks {
    Long boxedLongCount = 0L;
    long longCount = 0L;
    volatile long volatileLongCount = 0L;
    AtomicLong atomicLong = new AtomicLong();
    LongAdder longAdder = new LongAdder();

    @Benchmark
    public void plainLongRead(Blackhole bh) {
        bh.consume(longCount);
    }

    @Benchmark
    public void plainLongWrite() {
        longCount++;
    }

    @Benchmark
    public void boxedLongRead(Blackhole bh) {
        bh.consume(boxedLongCount);
    }

    @Benchmark
    public void boxedLongWrite() {
        boxedLongCount++;
    }

    @Benchmark
    public void volatileLongRead(Blackhole bh) {
        bh.consume(volatileLongCount);
    }

    @Benchmark
    public void volatileLongWrite() {
        volatileLongCount++;
    }

    @Benchmark
    public void atomicLongRead(Blackhole bh) {
        bh.consume(atomicLong.get());
    }

    @Benchmark
    public void atomicLongWrite() {
        atomicLong.getAndIncrement();
    }

    @Benchmark
    public void longAdderRead(Blackhole bh) {
        bh.consume(longAdder.sum());
    }

    @Benchmark
    public void longAdderWrite(Blackhole bh) {
        longAdder.increment();
    }
}
