package personal.xlpeng.benchmarks.allocationlatency;

import org.HdrHistogram.Histogram;
import org.HdrHistogram.LinearIterator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;


public class AllocationLatency {
    static volatile byte[] sink;
    static long WARMUP_TIME_MSEC = 10000;
    static long RUN_TIME_MSEC = 15000;
    static int[] DATA_SIZES = {10, 100, 1000, 10000, 100000, 1_000_000, 10_000_000, 100_000_000};

    static void recordTimeToAllocate(final int dataSize, final Histogram histogram) {
        long startTime = System.nanoTime();
        sink = new byte[dataSize];
        long endTime = System.nanoTime();
        histogram.recordValue(endTime - startTime);
    }

    static Histogram runAllocationTest(final int dataSize) {
        Histogram histogram = new Histogram(3600000000000L, 3);

        long startTime = System.currentTimeMillis();
        long now;
        do {
            recordTimeToAllocate(dataSize, histogram);
            now = System.currentTimeMillis();
        } while (now - startTime < WARMUP_TIME_MSEC);

        histogram.reset();

        histogram.setStartTimeStamp(System.currentTimeMillis());
        do {
            recordTimeToAllocate(dataSize, histogram);
            now = System.currentTimeMillis();
        } while (now - startTime < RUN_TIME_MSEC);
        histogram.setEndTimeStamp(System.currentTimeMillis());

        return histogram;
    }

    public static void main(final String[] args) throws IOException, InterruptedException {

        for (int dataSize : DATA_SIZES) {
            final Histogram histogram = runAllocationTest(dataSize);
            final Instant startTime = Instant.ofEpochMilli(histogram.getStartTimeStamp());
            final Instant endTime = Instant.ofEpochMilli(histogram.getEndTimeStamp());
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(".", "allocation-latency-" + dataSize + "-" + startTime + "-" + endTime + ".csv"), StandardOpenOption.CREATE)) {
                bufferedWriter.write("Latency(100us), Count");
                bufferedWriter.newLine();
                new LinearIterator(histogram, 100_000)
                        .forEachRemaining(value -> {
                            if (value.getCountAddedInThisIterationStep() > 0) {
                                try {
                                    bufferedWriter.write(String.valueOf((value.getValueIteratedTo() + 1) / 100_000));
                                    bufferedWriter.write(",");
                                    bufferedWriter.write(String.valueOf(value.getCountAddedInThisIterationStep()));
                                    bufferedWriter.newLine();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
            }

            System.gc();
            Thread.sleep(3000);
        }
    }
}
