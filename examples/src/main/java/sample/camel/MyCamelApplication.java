package sample.camel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A sample Spring Boot application that starts the Camel routes.
 */
@SpringBootApplication
public class MyCamelApplication {

    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(MyCamelApplication.class, args);
    }

}
