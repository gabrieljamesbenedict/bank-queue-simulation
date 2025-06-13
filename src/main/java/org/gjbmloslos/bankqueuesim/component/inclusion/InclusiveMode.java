package org.gjbmloslos.bankqueuesim.component.inclusion;

import javafx.application.Platform;
import org.gjbmloslos.bankqueuesim.entity.bank.BankTeller;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerQueue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class InclusiveMode extends InclusionMode{

    @Override
    public String getMode () {
        return "Inclusive";
    }

    @Override
    public void assignCustomerToTeller(ArrayList<BankTeller> availableBankTellerList) {
        try {

            Random r = new Random();
            //System.out.println(availableBankTellerList);

            bankTellerGuard.acquire();
            try {
                Iterator<BankTeller> bti = availableBankTellerList.iterator();
                while (bti.hasNext()) {

                    BankTeller bt = bti.next();

                    CustomerQueue cq = customerQueueList.get(r.nextInt(customerQueueList.size()));
                    if (cq.getCustomerQueue().isEmpty()) {
                        return;
                    }

                    Customer c = cq.getCustomerQueue().remove();

                    System.out.println(log(c,bt));
                    c.setProcessing(true);
                    bt.setCurrentCustomer(c);
                    Platform.runLater(() -> {
                        if (bt.getTellerBox().getChildren().size() > 1)
                            bt.getTellerBox().getChildren().set(1, c.getLabelRef());
                        else
                            bt.getTellerBox().getChildren().add(c.getLabelRef());
                    });
                    bt.setBusy(true);
                    availableBankTellerList.remove(bt);

                }
            } finally {
                bankTellerGuard.release();
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }
}
