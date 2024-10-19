package org.jaxtech.jaxtech.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jaxtech.jaxtech.modelo.Usuario;

import java.util.Arrays;

/**
 * Controlador para la creación y modificación de usuarios.
 */
public class CrearUsuario {
    // Campos de texto para ingresar los datos del usuario
    @FXML
    public TextField fidelNombre;
    @FXML
    public TextField fidelApellido;
    @FXML
    public TextField fidelDireccion;
    @FXML
    public ChoiceBox<String> choiceTipoPago;
    @FXML
    public Label textTitulo;
    @FXML
    public TextField fidelTelefono;
    @FXML
    public TextField fidelPassword;
    @FXML
    public CheckBox checkAdmin;

    // Usuario actual
    Usuario usuario;
    // Referencia a la pantalla de administración
    PantallaAdmin pantallaAdmin;

    /**
     * Método de inicialización sin usuario.
     * @param pantallaAdmin Referencia a la pantalla de administración.
     * @param tiposPago Tipos de pago disponibles.
     */
    public void inicio(PantallaAdmin pantallaAdmin, String[] tiposPago) {
        this.pantallaAdmin = pantallaAdmin;
        Arrays.stream(tiposPago).filter(tipo -> !tipo.equals("No filtros")).forEach(tipo -> choiceTipoPago.getItems().add(tipo));
    }

    /**
     * Método de inicialización con usuario.
     * @param pantallaAdmin Referencia a la pantalla de administración.
     * @param tiposPago Tipos de pago disponibles.
     * @param usuario Usuario a modificar.
     */
    public void inicio(PantallaAdmin pantallaAdmin, String[] tiposPago, Usuario usuario) {
        this.pantallaAdmin = pantallaAdmin;
        choiceTipoPago.getItems().addAll(tiposPago);
        fidelNombre.setText(usuario.getNombre());
        fidelApellido.setText(usuario.getApellidos());
        fidelTelefono.setText(usuario.getNumTelefono());
        fidelDireccion.setText(usuario.getDireccion());
        choiceTipoPago.setValue(usuario.getTipoPago());
        this.usuario = usuario;
        textTitulo.setText("Modificar Usuario");
    }

    /**
     * Método para guardar o actualizar un usuario.
     */
    public void guardarUsuario() {
        // Validación de campos vacíos
        if (fidelNombre.getText().isEmpty() || fidelApellido.getText().isEmpty() || fidelDireccion.getText().isEmpty() || choiceTipoPago.getValue() == null || fidelTelefono.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Campos vacíos");
            alert.setContentText("Por favor, rellene todos los campos");
            alert.showAndWait();
            return;
        }
        // Creación de un nuevo usuario
        if (usuario == null) {
            if (fidelPassword.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Contraseña vacía");
                alert.setContentText("Por favor, rellene la contraseña");
                alert.showAndWait();
                return;
            }
            usuario = new Usuario(
                    fidelNombre.getText(),
                    fidelApellido.getText(),
                    fidelDireccion.getText(),
                    choiceTipoPago.getValue(),
                    fidelTelefono.getText(), 0,
                    fidelPassword.getText(),
                    checkAdmin.isSelected()
            );
            if (!usuario.insert())
                usuario = null;
            pantallaAdmin.agregarUsuario(usuario);
        } else {
            // Actualización de un usuario existente
            if (!fidelPassword.getText().isEmpty())
                usuario.setPassword(fidelPassword.getText());
            usuario.setNombre(fidelNombre.getText());
            usuario.setApellidos(fidelApellido.getText());
            usuario.setNumTelefono(fidelTelefono.getText());
            usuario.setDireccion(fidelDireccion.getText());
            usuario.setTipoPago(choiceTipoPago.getValue());
            usuario.update();
        }
        pantallaAdmin.actualizarTabla();

        // Cierre de la ventana actual
        Stage stage = (Stage) fidelNombre.getScene().getWindow();
        stage.close();
    }
}