package org.gjbmloslos.bankqueuesim.component.inclusion;

import javafx.application.Platform;
import org.gjbmloslos.bankqueuesim.entity.bank.BankTeller;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerQueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class ExclusiveMode extends InclusionMode {

    @Override
    public String getMode () {
        return "Exclusive";
    }

    @Override
    public void assignCustomerToTeller(ArrayList<BankTeller> availableBankTellerList) {

        for (BankTeller bt : bankTellerList) {
            if (!availableBankTellerList.contains(bt) || bt.isBusy()) continue;
            int index = bankTellerList.indexOf(bt);

            try {
                bankTellerGuard.acquire();
            } catch (InterruptedException e) {e.printStackTrace();}

            boolean myQueueEmpty = customerQueueList.get(index).getCustomerQueue().isEmpty();
            Customer c = (myQueueEmpty)? takeFromOtherQueue() : takeFromOwnQueue(index);
            if (c == null) {
                bankTellerGuard.release();
                return;
            };

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

            bankTellerGuard.release();
        }
    }

    private Customer takeFromOwnQueue (int index) {
        return customerQueueList.get(index).getCustomerQueue().remove();
    }

    private Customer takeFromOtherQueue () {
        for (CustomerQueue cq : customerQueueList) {
            if (cq.getCustomerQueue().isEmpty()) continue;
            return cq.getCustomerQueue().remove();
        }
        return null;
    }
}
