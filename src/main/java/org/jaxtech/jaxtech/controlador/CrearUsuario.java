package org.jaxtech.jaxtech.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jaxtech.jaxtech.modelo.Usuario;

import java.util.Arrays;

public class CrearUsuario {
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

    Usuario usuario;
    PantallaAdmin pantallaAdmin;

    public void inicio(PantallaAdmin pantallaAdmin, String[] tiposPago) {
        this.pantallaAdmin = pantallaAdmin;
        Arrays.stream(tiposPago).filter(tipo -> !tipo.equals("No filtros")).forEach(tipo -> choiceTipoPago.getItems().add(tipo));
    }

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

    public void guardarUsuario() {
        if (fidelNombre.getText().isEmpty() || fidelApellido.getText().isEmpty() || fidelDireccion.getText().isEmpty() || choiceTipoPago.getValue() == null || fidelTelefono.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Campos vacíos");
            alert.setContentText("Por favor, rellene todos los campos");
            alert.showAndWait();
            return;
        }
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

        Stage stage = (Stage) fidelNombre.getScene().getWindow();
        stage.close();
    }
}
