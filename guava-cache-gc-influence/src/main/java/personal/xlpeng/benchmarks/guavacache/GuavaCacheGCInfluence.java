package personal.xlpeng.benchmarks.guavacache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.util.Map;
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

    public static Map<Integer, byte[]> offHeapCache = ChronicleMapBuilder.of(Integer.class, byte[].class)
            .averageValueSize(3072)
            .entries(60000)
            .create();

    public static Random random = new Random();

    public static void main(String[] args) {
        final boolean useCache = Set.of(args).contains("--use-cache");
        final boolean offHeap = Set.of(args).contains("--off-heap");
        final long start = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            new Thread() {
                private volatile byte[] sink;
                @Override
                public void run() {
                    while(System.currentTimeMillis() - start < 300000) {
                        sink = useCache ? (offHeap ? createDummyDataWithOffHeapCache() : createDummyDataWithCache()) : createDummyData();
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

    public static byte[] createDummyDataWithOffHeapCache() {
        Integer key = random.nextInt(100000);
        if (!offHeapCache.containsKey(key)) {
            offHeapCache.put(key, createDummyData());
        }
        return offHeapCache.get(key);
    }
}
