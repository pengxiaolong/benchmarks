package personal.xlpeng.benchmarks.springframework.beanfactory;

public class AService {
    public Configurations configs;

    public AService(Configurations configs) {
        this.configs = configs;
    }
}
