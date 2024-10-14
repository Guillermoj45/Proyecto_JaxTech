package org.jaxtech.jaxtech.controlador;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jaxtech.jaxtech.modelo.DDBB;
import org.jaxtech.jaxtech.modelo.Producto;
import org.jaxtech.jaxtech.modelo.Usuario;

import java.io.File;
import java.io.IOException;

public class AgregarProducto {

    @FXML
    public TextField nombreTextFiled;
    @FXML
    public TextField stockTextFiled;
    @FXML
    public TextField precioTextFiled;
    @FXML
    public ChoiceBox<String> tipoChoice;
    @FXML
    public ImageView productoImg;
    @FXML
    public Image imagen, imagenDefecto;
    @FXML
    public TableView<Usuario> tablaUsuarios;
    @FXML
    public TableColumn<Usuario, Integer> columnTotalPedidos;
    @FXML
    public TableColumn<Usuario, String> columNumTelefono;
    @FXML
    public TableColumn<Usuario, String> columTipoPago;
    @FXML
    public TableColumn<Usuario, String> columDireccion;
    @FXML
    public TableColumn<Usuario, String> columNombre;
    @FXML
    public TableColumn<Usuario, String> columApellidos;
    @FXML
    public TableColumn<Usuario, Integer> columID;
    @FXML
    public Button buttModificarUsuario, buttCrearUsuario, buttEliminarUsuario;
    @FXML
    public ChoiceBox<String> filtroPagoChoice;
    @FXML
    public CheckBox checkAptoGaming;
    private ObservableList<Usuario> usuarios, usuariosfiltrados;
    public String[] tiposPago = {"No filtros", "Tarjeta", "Efectivo", "Paypal", "Físico", "Otro"};


    @FXML
    public void initialize() {
        new DDBB();
        tipoChoice.getItems().addAll("Teclado", "Ratón", "Camara", "Auriculares", "Monitor", "Portatil", "Sobremesa", "Otros");
        filtroPagoChoice.getItems().addAll(tiposPago);
        filtroPagoChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filtroUsuariosPorPago());

        usuarios = Usuario.getUsuarios();

        tablaUsuarios.setItems(usuarios);
        columID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        columApellidos.setCellValueFactory(cellData -> cellData.getValue().apellidosProperty());
        columDireccion.setCellValueFactory(cellData -> cellData.getValue().direccionProperty());
        columTipoPago.setCellValueFactory(cellData -> cellData.getValue().tipoPagoProperty());
        columNumTelefono.setCellValueFactory(cellData -> cellData.getValue().numTelefonoProperty());
        columnTotalPedidos.setCellValueFactory(cellData -> cellData.getValue().totalPedidosProperty().asObject());

        imagenDefecto = productoImg.getImage();
    }

    public void setScene(Scene scene) {
        ((Stage) scene.getWindow()).close();
    }

    public void activarBotones() {
        buttEliminarUsuario.setDisable(false);
        buttModificarUsuario.setDisable(false);
    }

    public void eliminarUsario() {
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        usuario.delete();
        usuarios.remove(usuario);
        if (usuariosfiltrados != null)
            usuariosfiltrados.remove(usuario);
    }

    public void crearUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jaxtech/jaxtech/crear_usuario.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Crear Usuario");
            stage.show();

            CrearUsuario controller = loader.getController();
            AgregarProducto agregarProducto = this;
            controller.inicio(agregarProducto, tiposPago);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void modificarUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jaxtech/jaxtech/crear_usuario.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modificar Usuario");
            stage.show();

            CrearUsuario controller = loader.getController();
            AgregarProducto agregarProducto = this;
            controller.inicio(agregarProducto, tiposPago, tablaUsuarios.getSelectionModel().getSelectedItem());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        if (usuariosfiltrados != null)
            usuariosfiltrados.add(usuario);
    }

    public void actualizarTabla() {
        tablaUsuarios.refresh();
    }

    public void filtroUsuariosPorPago() {
        String filtro = filtroPagoChoice.getValue();
        usuariosfiltrados = FXCollections.observableArrayList();

        if (!filtro.equals("No filtros")) {
            usuariosfiltrados.clear();
            for (Usuario usuario : usuarios) {
                if (usuario.getTipoPago().equals(filtro)) {
                    usuariosfiltrados.add(usuario);
                }
            }
            tablaUsuarios.setItems(usuariosfiltrados);
        } else {
            usuariosfiltrados.clear();
            tablaUsuarios.setItems(usuarios);
        }
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
        float precio;
        int stock;
        try {
            precio = Float.parseFloat(precioTextFiled.getText());
        } catch (NumberFormatException e) {
            alerta("Error", "Precio no válido");
            return;
        }
        try {
            stock = Integer.parseInt(stockTextFiled.getText());
        } catch (NumberFormatException e) {
            alerta("Error", "Stock no válido");
            return;
        }

        String nombre = nombreTextFiled.getText();
        String tipo = tipoChoice.getValue();
        Image imagen = productoImg.getImage();
        boolean aptoGaming = checkAptoGaming.isSelected();

        if (nombre.isEmpty() || tipo == null || imagen == null) {
            alerta("Error", "Campos vacíos");
            return;
        }
        System.out.println("Nombre: " + nombre + ", Stock: " + stock + ", Dimensiones: " + precio + ", Tipo: " + tipo + ", Imagen: " + imagen.getUrl());

        Producto producto = new Producto(nombre, tipo, stock, precio, imagen, aptoGaming);
        producto.insert();

        nombreTextFiled.clear();
        stockTextFiled.clear();
        precioTextFiled.clear();
        tipoChoice.setValue(null);
        productoImg.setImage(imagenDefecto);
        checkAptoGaming.setSelected(false);
    }

    private void alerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(mensaje);
        alert.showAndWait();
    }
}
