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


import org.jaxtech.jaxtech.CrearUsuario;
import org.jaxtech.jaxtech.modelo.Usuario;

import java.io.File;
import java.io.IOException;

public class AgregarProducto {

    @FXML
    public TextField nombreTextFiled;
    @FXML
    public TextField stockTextFiled;
    @FXML
    public TextField dimensionesTextFiled;
    @FXML
    public ChoiceBox<String> tipoChoice;
    @FXML
    public ImageView productoImg;
    @FXML
    public Image imagen;
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
    private ObservableList<Usuario> usuarios, usuariosfiltrados;
    public String[] tiposPago = {"No filtros", "Tarjeta", "Efectivo", "Paypal", "Físico", "Otro"};


    @FXML
    public void initialize() {
        tipoChoice.getItems().addAll("Teclado", "Ratón", "Camara", "Auriculares", "Monitor", "Portatil", "Sobremesa", "Otros");
        filtroPagoChoice.getItems().addAll(tiposPago);
        filtroPagoChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filtroUsuariosPorPago());

        usuarios = FXCollections.observableArrayList();

        tablaUsuarios.setItems(usuarios);
        columID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        columApellidos.setCellValueFactory(cellData -> cellData.getValue().apellidosProperty());
        columDireccion.setCellValueFactory(cellData -> cellData.getValue().direccionProperty());
        columTipoPago.setCellValueFactory(cellData -> cellData.getValue().tipoPagoProperty());
        columNumTelefono.setCellValueFactory(cellData -> cellData.getValue().numTelefonoProperty());
        columnTotalPedidos.setCellValueFactory(cellData -> cellData.getValue().totalPedidosProperty().asObject());

        usuarios.add(new Usuario(1, "Juan", "Perez", "Calle Falsa 123", "Tarjeta", "123456789", 5));
        usuarios.add(new Usuario(2, "Maria", "Gonzalez", "Calle Falsa 456", "Efectivo", "987654321", 3));
        usuarios.add(new Usuario(3, "Pedro", "Ramirez", "Calle Falsa 789", "Paypal", "456789123", 7));
        usuarios.add(new Usuario(4, "Ana", "Martinez", "Calle Falsa 012", "Tarjeta", "789123456", 2));
        usuarios.add(new Usuario(5, "Luis", "Lopez", "Calle Falsa 345", "Efectivo", "321654987", 4));
        usuarios.add(new Usuario(6, "Sara", "Fernandez", "Calle Falsa 678", "Paypal", "654987321", 6));
        usuarios.add(new Usuario(7, "Carlos", "Sanchez", "Calle Falsa 901", "Tarjeta", "987321654", 1));
    }

    public void activarBotones(){
        buttEliminarUsuario.setDisable(false);
        buttModificarUsuario.setDisable(false);
    }

    public void eliminarUsario(){
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        usuarios.remove(usuario);
        if (usuariosfiltrados != null)
            usuariosfiltrados.remove(usuario);
    }

    public void crearUsuario(){
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
            controller.inicio(agregarProducto,tiposPago);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void modificarUsuario(){
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
            controller.inicio(agregarProducto,tiposPago,tablaUsuarios.getSelectionModel().getSelectedItem());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void agregarUsuario(Usuario usuario){
        usuarios.add(usuario);
        if (usuariosfiltrados != null)
            usuariosfiltrados.add(usuario);
    }

    public void actualizarTabla(){
        tablaUsuarios.refresh();
    }

    public void filtroUsuariosPorPago(){
        String filtro = filtroPagoChoice.getValue();
        usuariosfiltrados = FXCollections.observableArrayList();

        if(!filtro.equals("No filtros")){
            usuariosfiltrados.clear();
            for(Usuario usuario : usuarios){
                if(usuario.getTipoPago().equals(filtro)){
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
