package personal.xlpeng.benchmarks.jmh;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class MapIterationBenchmark {

    public Map<String, String> linked;
    public Map<String, String> hash;
    public Map<String, String> unmodifiableHash;
    public Map<String, String> unmodifiableLinked;

    private Object iterate(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        return iterator;
    }

    @Setup
    public void setup() {
        hash = new HashMap<>();
        hash.put("1", null);
        hash.put("2", null);
        hash.put("3", null);

        linked = new LinkedHashMap<>(hash);
        unmodifiableHash = Collections.unmodifiableMap(hash);
        unmodifiableLinked = Collections.unmodifiableMap(linked);

        // trigger the iteration across each of the three types
        new HashMap<>(hash);
        new HashMap<>(linked);
        new HashMap<>(unmodifiableHash);
        System.gc();
    }

    @Benchmark
    public Object testIterateHash_Unmodifiable() {
        return iterate(unmodifiableHash);
    }

    @Benchmark
    public Object testIterateHashMap() {
        return iterate(hash);
    }

    @Benchmark
    public Object testIterateLinked() {
        return iterate(linked);
    }

    @Benchmark
    public Object testIterateLinked_Unmodifiable() {
        return iterate(unmodifiableLinked);
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*\\." + MapIterationBenchmark.class.getSimpleName() + "\\..*")
                .param("-prof", "perfasm:events=task-clock")
                .build();

        new Runner(opt).run();
    }
}