package service.core;

public class ServiceInfo {
    private String name = "service";
    private String url = "http://localhost:8080";

    public ServiceInfo(String name, String url){
        this.name = name;
        this.url = url;
    }

    public ServiceInfo(){}

    public String getName() { return name; }
    public String getUrl() { return url; }
}
