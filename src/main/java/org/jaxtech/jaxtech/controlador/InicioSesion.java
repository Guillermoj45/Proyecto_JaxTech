package org.jaxtech.jaxtech.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jaxtech.jaxtech.HelloApplication;
import org.jaxtech.jaxtech.modelo.DDBB;
import org.jaxtech.jaxtech.modelo.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controlador para gestionar el inicio de sesión.
 */
public class InicioSesion {

    // Campo de texto para la contraseña
    @FXML
    private PasswordField textFiledPassword;

    // Campo de texto para el nombre de usuario
    @FXML
    private TextField textFiledUsuario;

    /**
     * Método que se ejecuta al iniciar sesión.
     * @param event El evento de acción.
     */
    @FXML
    void iniciarSesion(ActionEvent event) {
        int id;
        boolean admin;
        // Verifica si los campos de usuario y contraseña están vacíos
        if (textFiledUsuario.getText().isEmpty() || textFiledPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Campos vacios");
            alert.setContentText("Por favor, rellene todos los campos");
            alert.showAndWait();
        } else {
            // Conexión a la base de datos
            Connection conexion = DDBB.getConexion();
            String sql = "SELECT id, nombre, apellidos, direccion, pago, telefono, admin FROM usuarios WHERE nombre = ? AND password = password(?) limit 1";
            Usuario usuario;
            try {
                // Prepara y ejecuta la consulta SQL
                PreparedStatement select = conexion.prepareStatement(sql);
                select.setString(1, textFiledUsuario.getText());
                select.setString(2, textFiledPassword.getText());

                ResultSet resultado = select.executeQuery();
                // Verifica si se encontró un usuario con las credenciales proporcionadas
                if (resultado.next()) {
                    id = resultado.getInt("id");
                    admin = resultado.getBoolean("admin");
                    String nombreUsuario = resultado.getString("nombre");
                    String apellidos = resultado.getString("apellidos");
                    String direccion = resultado.getString("direccion");
                    String pago = resultado.getString("pago");
                    String telefono = resultado.getString("telefono");

                    usuario = new Usuario(id, nombreUsuario, apellidos, direccion, pago, telefono, admin);
                } else {
                    // Muestra un mensaje de error si las credenciales son incorrectas
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Usuario o contraseña incorrectos");
                    alert.setContentText("Por favor, revise los datos introducidos");
                    alert.showAndWait();
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                // Carga la pantalla correspondiente según el tipo de usuario (admin o cliente)
                Scene scene = textFiledUsuario.getScene();
                if (usuario.isAdmin()) {
                    cargarPantallaAdministrador(scene);
                } else {
                    cargarPantallaUsuario(usuario, scene);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para cargar la pantalla de usuario.
     * @param usuario El usuario actual.
     * @param scene La escena actual.
     * @throws IOException Si ocurre un error al cargar la pantalla.
     */
    private void cargarPantallaUsuario(Usuario usuario, Scene scene) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/jaxtech/jaxtech/pantalla_clientes.fxml"));
        Parent root = fxmlLoader.load();
        Scene nuevaScena = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(nuevaScena);
        PantallaClientes pantallaClientes = fxmlLoader.getController();
        pantallaClientes.setUsuario(usuario);
        pantallaClientes.setScene(scene);
        stage.setTitle("Cliente");
        stage.show();
    }

    /**
     * Método para cargar la pantalla de administrador.
     * @param scene La escena actual.
     * @throws IOException Si ocurre un error al cargar la pantalla.
     */
    private void cargarPantallaAdministrador(Scene scene) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/jaxtech/jaxtech/PantallaAdmin.fxml"));
        Parent root = fxmlLoader.load();
        Scene nuevaScena = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(nuevaScena);
        stage.setTitle("Administrador");
        stage.show();
        PantallaAdmin pantallaAdmin = fxmlLoader.getController();
        pantallaAdmin.setScene(scene);
    }
}