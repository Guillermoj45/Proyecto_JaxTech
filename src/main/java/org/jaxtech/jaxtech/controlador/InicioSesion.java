package org.jaxtech.jaxtech.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jaxtech.jaxtech.HelloApplication;
import org.jaxtech.jaxtech.modelo.DDBB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InicioSesion {

    @FXML
    private TextField textFiledPassword;

    @FXML
    private TextField textFiledUsuario;

    @FXML
    void iniciarSesion(ActionEvent event) {
        int id;
        boolean admin = false;
        if (textFiledUsuario.getText().isEmpty() || textFiledPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Campos vacios");
            alert.setContentText("Por favor, rellene todos los campos");
            alert.showAndWait();
        } else {
            Connection conexion = DDBB.getConexion();
            String sql = "SELECT id, admin FROM usuarios WHERE nombre = ? AND password = password(?) limit 1";
            try {
                PreparedStatement select = conexion.prepareStatement(sql);
                select.setString(1, textFiledUsuario.getText());
                select.setString(2, textFiledPassword.getText());

                ResultSet resultado = select.executeQuery();
                if (resultado.next()) {
                    id = resultado.getInt("id");
                    admin = resultado.getBoolean("admin");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Usuario o contraseña incorrectos");
                    alert.setContentText("Por favor, revise los datos introducidos");
                    alert.showAndWait();
                }
                Scene scene = textFiledUsuario.getScene();
                if (admin) {

                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/jaxtech/jaxtech/agregar_producto.fxml"));
                    Parent root = fxmlLoader.load();
                    Scene nuevaScena = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(nuevaScena);
                    stage.setTitle("Administrador");
                    stage.show();


                    AgregarProducto agregarProducto = fxmlLoader.getController();
                    agregarProducto.setScene(scene);
                } else {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/jaxtech/jaxtech/pantalla_clientes.fxml"));
                    Parent root = fxmlLoader.load();
                    Scene nuevaScena = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(nuevaScena);
                    PantallaClientes pantallaClientes = fxmlLoader.getController();
                    pantallaClientes.setScene(scene);
                    stage.setTitle("Cliente");
                    stage.show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
