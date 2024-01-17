package service.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.broker.BrokerService;
import service.core.Application;
import service.core.ClientInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@RestController
public class BrokerController {
    private final TreeMap<Integer, Application> applications = new TreeMap<>();
    private final BrokerService brokerService = new BrokerService();
    @Value("${server.port}")
    private int port;

    @GetMapping(value = "/applications", produces = "application/json")
    public ResponseEntity<ArrayList<String>> getApplications(){
        ArrayList<String> list = new ArrayList<>();
        for (Application a : applications.values()){
            list.add("http://"+getHost()+"/applications/"+a.id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping(value="/applications/{id}", produces={"application/json"})
    public ResponseEntity<Application> getApplication(@PathVariable Integer id) {
        Application application = applications.get(id);
        if (application == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }

    @PostMapping(value = "/applications", consumes = "application/json")
    public ResponseEntity<Application> createApplication(@RequestBody ClientInfo info){
        Application application = new Application(info);
        application.quotations = brokerService.getQuotations(info);
        applications.put(application.id, application);
        String url = "http://"+getHost()+"/applications/"
                + application.id;
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", url)
                .header("Content-Location", url)
                .body(application);
    }

    private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            return "localhost:" + port;
        }
    }
}
