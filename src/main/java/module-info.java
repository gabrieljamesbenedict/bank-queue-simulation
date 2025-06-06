module org.gjbmloslos.bankqueuesim {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.gjbmloslos.bankqueuesim to javafx.fxml;
    opens org.gjbmloslos.bankqueuesim.model to javafx.base;

    exports org.gjbmloslos.bankqueuesim;
}