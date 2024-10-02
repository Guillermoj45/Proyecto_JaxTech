package org.jaxtech.jaxtech;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jaxtech.jaxtech.controlador.AgregarProducto;
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
    public ChoiceBox choiceTipoPago;
    @FXML
    public Label textTitulo;
    @FXML
    public TextField fidelTelefono;

    Usuario usuario;
    AgregarProducto agregarProducto;

    public void inicio(AgregarProducto agregarProducto, String[] tiposPago) {
        this.agregarProducto = agregarProducto;
        Arrays.stream(tiposPago).filter(tipo -> !tipo.equals("No filtros")).forEach(tipo -> choiceTipoPago.getItems().add(tipo));
    }
    public void inicio(AgregarProducto agregarProducto, String[] tiposPago, Usuario usuario) {
        this.agregarProducto = agregarProducto;
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
        if (fidelNombre.getText().isEmpty() || fidelApellido.getText().isEmpty() || fidelDireccion.getText().isEmpty() || choiceTipoPago.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Campos vacíos");
            alert.setContentText("Por favor, rellene todos los campos");
            alert.showAndWait();
            return;
        }
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setNombre(fidelNombre.getText());
            usuario.setApellidos(fidelApellido.getText());
            usuario.setNumTelefono(fidelTelefono.getText());
            usuario.setDireccion(fidelDireccion.getText());
            usuario.setTipoPago(choiceTipoPago.getValue().toString());
            agregarProducto.agregarUsuario(usuario);
        } else {
            usuario.setNombre(fidelNombre.getText());
            usuario.setApellidos(fidelApellido.getText());
            usuario.setNumTelefono(fidelTelefono.getText());
            usuario.setDireccion(fidelDireccion.getText());
            usuario.setTipoPago(choiceTipoPago.getValue().toString());
        }
        agregarProducto.actualizarTabla();

        Stage stage = (Stage) fidelNombre.getScene().getWindow();
        stage.close();
    }
}
