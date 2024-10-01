package org.jaxtech.jaxtech;

import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class AgregarProducto {

    public TextField nombreTextFiled;
    public TextField stockTextFiled;
    public TextField dimensionesTextFiled;
    public ChoiceBox<String> tipoChoice;
    public ImageView productoImg;
    public Image imagen;

    public void initialize() {
        tipoChoice.getItems().addAll("Teclado", "Ratón", "Camara", "Auriculares", "Monitor", "Portatil", "Sobremesa", "Otros");
    }

    public void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String imagePath = selectedFile.toURI().toString();
                imagen = new Image(imagePath);
                productoImg.setImage(imagen);
            } catch (Exception e) {
                e.printStackTrace();
                // Maneja la excepción según sea necesario
            }
        }
    }

    public void agregarProducto() {
        String nombre = nombreTextFiled.getText();
        String stock = stockTextFiled.getText();
        String dimensiones = dimensionesTextFiled.getText();
        String tipo = tipoChoice.getValue();
        if (nombre.isEmpty() || stock.isEmpty() || dimensiones.isEmpty() || tipo == null || imagen == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al agregar el producto");
            alert.setContentText("Por favor, rellene todos los campos");
            alert.show();
            return;
        }

        System.out.println("Nombre: " + nombre + ", Stock: " + stock + ", Dimensiones: " + dimensiones + ", Tipo: " + tipo);
        // Guardar la imagen en la base de datos
        // Guardar el producto en la base de datos
    }
}
