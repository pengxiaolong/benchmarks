package personal.xlpeng.benchmarks.allocationlatency;

import org.HdrHistogram.Histogram;
import org.HdrHistogram.LinearIterator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;


public class MultiThreadAllocationLatency {
    static volatile byte[] sink;
    static final long WARMUP_TIME_MSEC = 10000;
    static final long RUN_TIME_MSEC = 20000;
    static final int[] DATA_SIZES = {10, 100, 1000, 10000, 100000, 1_000_000, 10_000_000, 100_000_000};
    static final int threadCount = Runtime.getRuntime().availableProcessors();

    static void recordTimeToAllocate(final int dataSize, final Histogram histogram) {
        long startTime = System.nanoTime();
        sink = new byte[dataSize];
        long endTime = System.nanoTime();
        histogram.recordValue(endTime - startTime);
    }

    static Histogram runAllocationTest(final int dataSize) {

        long startTime = System.currentTimeMillis();
        final Histogram totalHistogram = new Histogram(3600000000000L, 3);
        final CountDownLatch warmSignal = new CountDownLatch(1);
        final CountDownLatch warmed = new CountDownLatch(threadCount);
        final CountDownLatch startSignal = new CountDownLatch(1);
        final CountDownLatch finished = new CountDownLatch(threadCount);
        final Histogram[] histograms = new Histogram[threadCount];
        final Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final Histogram histogram = new Histogram(3600000000000L, 3);
            histograms[i] = histogram;
            threads[i] = new Thread(() -> {
                wait(warmSignal);
                long now;
                do {
                    recordTimeToAllocate(dataSize, histogram);
                    now = System.currentTimeMillis();
                } while (now - startTime < WARMUP_TIME_MSEC);
                histogram.reset();

                warmed.countDown();

                wait(startSignal);
                histogram.setStartTimeStamp(System.currentTimeMillis());
                do {
                    recordTimeToAllocate(dataSize, histogram);
                    now = System.currentTimeMillis();
                } while (now - startTime < RUN_TIME_MSEC);
                histogram.setEndTimeStamp(System.currentTimeMillis());
                finished.countDown();
            });
            threads[i].start();
        }

        warmSignal.countDown(); //Start to warm
        wait(warmed); // wait until warmed
        totalHistogram.setStartTimeStamp(System.currentTimeMillis());
        startSignal.countDown(); //Start to test
        wait(finished);
        totalHistogram.setEndTimeStamp(System.currentTimeMillis());

        for (Histogram histogram : histograms) {
            totalHistogram.add(histogram);
        }

        return totalHistogram;
    }

    public static void wait(final CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
