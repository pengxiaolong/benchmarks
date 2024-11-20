package personal.xlpeng.benchmarks.springframework.beanfactory;

import java.util.Map;

public class AServiceWithMap {
    Map<String, String> configs;

    public AServiceWithMap(Map<String, String> configs) {
        this.configs = configs;
    }
}
