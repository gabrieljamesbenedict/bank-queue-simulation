module org.gjbmloslos.bankqueuesim {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.gjbmloslos.bankqueuesim to javafx.fxml;
    exports org.gjbmloslos.bankqueuesim;
}