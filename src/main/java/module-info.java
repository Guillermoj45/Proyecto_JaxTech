module org.jaxtech.jaxtech {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.dotenv;
    requires java.sql;
    requires mysql.connector.j;

    opens org.jaxtech.jaxtech to javafx.fxml;
    exports org.jaxtech.jaxtech;
    exports org.jaxtech.jaxtech.modelo;
    opens org.jaxtech.jaxtech.modelo to javafx.fxml;
    exports org.jaxtech.jaxtech.controlador;
    opens org.jaxtech.jaxtech.controlador to javafx.fxml;
}