package springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Consumer1Application {
    public static void main(String[] args) {
        SpringApplication.run(Consumer1Application.class, args);
    }
}
