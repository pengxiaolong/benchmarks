package personal.xlpeng.benchmarks.allocationlatency;

import org.HdrHistogram.Histogram;

import java.net.DatagramSocket;

public class AllocationLatency {
    static volatile byte[] sink;
    // A Histogram covering the range from 1 nsec to 1 hour with 3 decimal point resolution:
    static Histogram histogram = new Histogram(3600000000000L, 3);

    static public volatile DatagramSocket socket;

    static long WARMUP_TIME_MSEC = 5000;
    static long RUN_TIME_MSEC = 20000;


    static void recordTimeToAllocate() {
        long startTime = System.nanoTime();
        sink = new byte[40000];
        long endTime = System.nanoTime();
        histogram.recordValue(endTime - startTime);
    }

    public static void main(final String[] args) {
        long startTime = System.currentTimeMillis();
        long now;

        do {
            recordTimeToAllocate();
            now = System.currentTimeMillis();
        } while (now - startTime < WARMUP_TIME_MSEC);

        histogram.reset();

        do {
            recordTimeToAllocate();
            now = System.currentTimeMillis();
        } while (now - startTime < RUN_TIME_MSEC);

        System.out.println("Recorded latencies [in usec] for array allocation:");

        histogram.outputPercentileDistribution(System.out, 1000.0);
    }

}
