package org.jaxtech.jaxtech;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jaxtech.jaxtech.modelo.DDBB;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/jaxtech/jaxtech/Inicio_sesion.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        new DDBB();
        stage.setTitle("JaxTech");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}