package service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import service.core.ServiceInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Value("${server.port}")
    private int port;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        String broker = "http://"+args[0]+"/services";
        String local = "http://"+getHost()+"/quotations";
        registerService(local, broker);
    }

    private static void registerService(String local, String broker) {
        ServiceInfo serviceInfo =
                new ServiceInfo("afqService", local);
        RestTemplate template = new RestTemplate();
        template.postForEntity(broker, serviceInfo, String.class);

    }

    private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            return "localhost:" + port;
        }
    }
}
