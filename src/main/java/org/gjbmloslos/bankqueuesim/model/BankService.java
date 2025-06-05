package org.gjbmloslos.bankqueuesim.model;

public class BankService {

    String serviceName;
    int serviceDuration;

    public BankService(String serviceName, int serviceDuration) {
        this.serviceName = serviceName;
        this.serviceDuration = serviceDuration;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(int serviceDuration) {
        this.serviceDuration = serviceDuration;
    }
}
