package org.gjbmloslos.bankqueuesim.component.inclusion;

import javafx.application.Platform;
import org.gjbmloslos.bankqueuesim.entity.bank.BankTeller;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;

import java.util.ArrayList;

public class StrictExclusiveMode extends InclusionMode{

    @Override
    public String getMode () {
        return "Strict Exclusive";
    }

    @Override
    public void assignCustomerToTeller(ArrayList<BankTeller> availableBankTellerList) {

        for (BankTeller bt : bankTellerList) {
            if (!availableBankTellerList.contains(bt) || bt.isBusy()) continue;
            int index = bankTellerList.indexOf(bt);
            if (customerQueueList.get(index).getCustomerQueue().isEmpty()) continue;

            try {
                bankTellerGuard.acquire();
            } catch (InterruptedException e) {e.printStackTrace();}

            Customer c = customerQueueList.get(index).getCustomerQueue().remove();

            System.out.println(log(c,bt));
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
}
