package service.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.broker.BrokerService;
import service.core.ServiceInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@RestController
public class RegistrationController {
    @Value("${server.port}")
    private int port;

    @GetMapping(value = "/services", produces = "application/json")
    public ResponseEntity<ArrayList<String>> getServices(){
        ArrayList<String> list = new ArrayList<>();
        for (ServiceInfo serviceInfo: BrokerService.services){
            list.add("http://"+getHost()+"/services/"+serviceInfo.getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping(value="/services/{name}", produces={"application/json"})
    public ResponseEntity<String> getService(@PathVariable String name) {
        for (ServiceInfo serviceInfo : BrokerService.services){
            if (serviceInfo.getName().equals(name)){
                return ResponseEntity.status(HttpStatus.OK).body(serviceInfo.getUrl());
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping(value = "/services", consumes = "application/json")
    public ResponseEntity<String> postService(@RequestBody ServiceInfo serviceInfo){
        BrokerService.services.add(serviceInfo);
        String locURL = "http://"+getHost()+"/services/"+serviceInfo.getName();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", locURL)
                .header("Content-Location", locURL)
                .body(serviceInfo.getUrl());
    }

    private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            return "localhost:" + port;
        }
    }
}
