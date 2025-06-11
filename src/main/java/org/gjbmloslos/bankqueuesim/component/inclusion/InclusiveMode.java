package org.gjbmloslos.bankqueuesim.component.inclusion;

import javafx.application.Platform;
import org.gjbmloslos.bankqueuesim.entity.bank.BankTeller;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerQueue;

import java.util.ArrayList;
import java.util.Iterator;

public class InclusiveMode extends InclusionMode{

    @Override
    public String getMode () {
        return "Inclusive";
    }

    @Override
    public void assignCustomerToTeller(ArrayList<BankTeller> availableBankTellerList) {
        if (availableBankTellerList.isEmpty()) return;

        try {
            bankTellerGuard.acquire();
        } catch (InterruptedException e) {e.printStackTrace();}

        BankTeller bt = availableBankTellerList
                .stream()
                .filter(e -> !e.isBusy())
                .toList()
                .getFirst();

        bankTellerGuard.release();

        Iterator<CustomerQueue> cqi = customerQueueList.iterator();
        while (cqi.hasNext()) {
            CustomerQueue cq = cqi.next();
            if (cq.getCustomerQueue().isEmpty()) continue;
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
            //System.out.println("Added Customer" + c.getId() + " from CustomerQueue" + c.getCustomerQueue().getId() + " to BankTeller" + bt.getId() + " " + timestamp());
        }
    }
}
