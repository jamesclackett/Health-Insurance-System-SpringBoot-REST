package service.broker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.ServiceInfo;

import java.util.ArrayList;

public class BrokerService {
    // Static so that it can be accessed by both Registration and Broker Controller
    public static ArrayList<ServiceInfo> services = new ArrayList<>();

    // Contact all 3 REST Quotation services for a given client
    public ArrayList<Quotation> getQuotations(ClientInfo info ){
        ArrayList<Quotation> quotations = new ArrayList<>();
        RestTemplate template = new RestTemplate();


        for (ServiceInfo serviceInfo : services){

            try {
                ResponseEntity<Quotation> response =
                        template.postForEntity(serviceInfo.getUrl(), info, Quotation.class);
                if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                    quotations.add(response.getBody());
                }
            } catch (Exception e){
                System.out.println(e.getLocalizedMessage());
            }
        }
        return quotations;
    }

}
