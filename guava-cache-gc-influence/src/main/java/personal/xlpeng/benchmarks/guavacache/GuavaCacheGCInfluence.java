package personal.xlpeng.benchmarks.guavacache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaCacheGCInfluence {
    public static Cache<Integer, byte[]> cache = CacheBuilder.newBuilder()
            .initialCapacity(60000)
            .maximumSize(60000)
            .concurrencyLevel(8)
            .expireAfterAccess(5184000, TimeUnit.SECONDS)
            .recordStats()
            .build();

    public static Random random = new Random();

    public static void main(String[] args) {
        final boolean useCache = Set.of(args).contains("--use-cache");
        final long start = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            new Thread() {
                private volatile byte[] sink;
                @Override
                public void run() {
                    while(System.currentTimeMillis() - start < 300000) {
                        sink = useCache ? createDummyDataWithCache() : createDummyData();
                    }
                }
            }.start();
        }
    }

    public static byte[] createDummyData() {
        return new byte[3072];
    }

    public static byte[] createDummyDataWithCache() {
        Integer key = random.nextInt(100000);
        try {
            return cache.get(key, GuavaCacheGCInfluence::createDummyData);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
