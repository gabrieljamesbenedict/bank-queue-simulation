module org.gjbmloslos.bankqueuesim {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports org.gjbmloslos.bankqueuesim;
    opens org.gjbmloslos.bankqueuesim to javafx.base, javafx.fxml;
    opens org.gjbmloslos.bankqueuesim.entity.interval to javafx.base;
    opens org.gjbmloslos.bankqueuesim.entity.customer to javafx.base;
    opens org.gjbmloslos.bankqueuesim.entity.queue to javafx.base;
    opens org.gjbmloslos.bankqueuesim.entity.bank to javafx.base;
}