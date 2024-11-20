package personal.xlpeng.benchmarks.springframework.beanfactory;

import java.util.Set;

public class AServiceWithSet {
    public Set<String> set;

    public AServiceWithSet(Set<String> set) {
        this.set = set;
    }
}
