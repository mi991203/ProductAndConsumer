package springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProductApplication {

    public static void main(String[] args) {
        final SpringApplication springApplication = new SpringApplication(ProductApplication.class);
        springApplication.run(args);
    }

}
