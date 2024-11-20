package personal.xlpeng.benchmarks.springframework.beanfactory;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;
import java.util.Set;

public class Main {
    static Map<String, String> config;
    static Set<String> stringSet;
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/beans.xml");
        while (true) {
            config = applicationContext.getBean(AServiceWithMap.class).configs;
        }
    }
}
