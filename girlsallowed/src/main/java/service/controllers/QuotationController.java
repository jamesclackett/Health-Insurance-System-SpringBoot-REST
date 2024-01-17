package service.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.girlsallowed.GAQService;
import service.core.ClientInfo;
import service.core.Quotation;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class QuotationController {
    private Map<String, Quotation> quotations = new TreeMap<>();
    private GAQService service = new GAQService();
    @Value("${server.port}")
    private int port;

    @GetMapping(value="/quotations", produces="application/json")
    public ResponseEntity<ArrayList<String>> getQuotations() {
        ArrayList<String> list = new ArrayList<>();
        for (Quotation q : quotations.values()){
            list.add("http://"+getHost()+"/quotations/"+q.reference);
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping(value="/quotations/{id}", produces={"application/json"}) //use {} if you want more types (eg xml)
    public ResponseEntity<Quotation> getQuotation(@PathVariable String id) {
        Quotation quotation = quotations.get(id);
        if (quotation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(quotation);
    }

    @PostMapping(value="/quotations", consumes = "application/json")
    public ResponseEntity<Quotation> createQuotation(@RequestBody ClientInfo info){
        Quotation quotation = service.generateQuotation(info);
        quotations.put(quotation.reference, quotation);
        String url = "http://"+getHost()+"/quotations/"
                + quotation.reference;
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", url)
                .header("Content-Location", url)
                .body(quotation);
    }

    private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            return "localhost:" + port;
        }
    }
}