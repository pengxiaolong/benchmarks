package personal.xlpeng.benchmarks.springframework.beanfactory;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 1)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class PrototypeBeansBenchmark {
    static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/beans.xml");
    static ApplicationContext applicationContextWithConversionService = new ClassPathXmlApplicationContext("classpath:/beans-conversion-service.xml");


//    static {
//        GenericConversionService conversionService = (GenericConversionService)applicationContextWithConversionService.getBean(ConversionService.class);
//        conversionService.removeConvertible(Map.class, Map.class);
//    }

    @Benchmark
    @Threads(16)
    public static void noMapInjection(Blackhole bh) {
        bh.consume(applicationContext.getBean(AService.class).configs.configs);
    }

    @Benchmark
    @Threads(16)
    public static void mapInjection(Blackhole bh) {
        bh.consume(applicationContext.getBean(AServiceWithMap.class).configs);
    }

    @Benchmark
    @Threads(16)
    public static void noMapInjectionWithConversionService(Blackhole bh) {
        bh.consume(applicationContextWithConversionService.getBean(AService.class).configs.configs);
    }

    @Benchmark
    @Threads(16)
    public static void mapInjectionWithConversionService(Blackhole bh) {
        bh.consume(applicationContextWithConversionService.getBean(AServiceWithMap.class).configs);
    }

}