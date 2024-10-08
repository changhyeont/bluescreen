package bluescreensite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "bluescreensite")
public class BluescreenApplication {

    public static void main(String[] args) {
        SpringApplication.run(BluescreenApplication.class, args);
    }

}
