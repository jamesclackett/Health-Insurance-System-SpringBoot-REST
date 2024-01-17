package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        // When the Quotation Services start, they post their address and service name
        // To the BrokerServices services list (see the Application classes)
    }
}
