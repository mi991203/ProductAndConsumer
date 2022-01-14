package springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringbootApplication {

    public static void main(String[] args) {
        final SpringApplication springApplication = new SpringApplication(SpringbootApplication.class);
        springApplication.run(args);
    }

}
