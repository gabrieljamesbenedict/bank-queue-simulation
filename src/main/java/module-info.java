module org.gjbmloslos.bankqueuesim {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports org.gjbmloslos.bankqueuesim;
    opens org.gjbmloslos.bankqueuesim to javafx.base, javafx.fxml;
    opens org.gjbmloslos.bankqueuesim.entity.customer to javafx.base;
    opens org.gjbmloslos.bankqueuesim.entity.bank to javafx.base;
    opens org.gjbmloslos.bankqueuesim.manager.bank to javafx.base;
    opens org.gjbmloslos.bankqueuesim.manager.customer to javafx.base;
    exports org.gjbmloslos.bankqueuesim.simulation;
    opens org.gjbmloslos.bankqueuesim.simulation to javafx.base, javafx.fxml;
}