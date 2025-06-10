package org.gjbmloslos.bankqueuesim.entity.bank;

import java.util.ArrayList;
import java.util.Random;

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

    public static BankService getRandomBankService (ArrayList<BankService> bankServiceList) {
        Random r = new Random();
        return bankServiceList.get(r.nextInt(bankServiceList.size()));
    }
}
