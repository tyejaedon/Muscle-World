module com.muscleflex.muscleflex {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires fontawesomefx;
    requires org.kordamp.ikonli.fontawesome5;
    requires mysql.connector.java;

    opens com.muscleflex.muscleflex to javafx.fxml;
    exports com.muscleflex.muscleflex;
}