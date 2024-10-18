package org.jaxtech.jaxtech.controlador;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jaxtech.jaxtech.modelo.DDBB;
import org.jaxtech.jaxtech.modelo.DatosAnalisis;
import org.jaxtech.jaxtech.modelo.Producto;
import org.jaxtech.jaxtech.modelo.Usuario;

import java.io.File;
import java.io.IOException;

import static javafx.collections.FXCollections.observableArrayList;

public class PantallaAdmin {

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
    @FXML
    public AreaChart<String, Double> productosComprados, cantidadDeVentas, dineroGenerado;
    public TableView tablaMasVendido;
    public TableColumn<DatosAnalisis,String> columTablaMasVendidoNombre;
    public TableColumn<DatosAnalisis,Integer> columTablaMasVendidoNVentas,columTablaMasVendidoUVendidas;
    public TableColumn<DatosAnalisis, Double> columTablaMasVendidoGanancias;


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

        columTablaMasVendidoNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        columTablaMasVendidoNVentas.setCellValueFactory(cellData -> cellData.getValue().nVentasProperty().asObject());
        columTablaMasVendidoUVendidas.setCellValueFactory(cellData -> cellData.getValue().uVendidasProperty().asObject());
        columTablaMasVendidoGanancias.setCellValueFactory(cellData -> cellData.getValue().gananciasProperty().asObject());
        tablaMasVendido.setItems(DatosAnalisis.datosTabla());


        ObservableList<XYChart.Data<String, Double>> datosVentasAnuales = DatosAnalisis.cantidadComprasAnuales();
        ObservableList<XYChart.Data<String, Double>> cantidadTotalAnual = DatosAnalisis.cantidadDeProductosComprados();
        ObservableList<XYChart.Data<String, Double>> gananciasAnuales = DatosAnalisis.gananciasAnuales();


        // Crear serie y agregar lista de datos
        XYChart.Series<String, Double> serieVentasAnulaes = new XYChart.Series<>("Ventas anuales", datosVentasAnuales);
        XYChart.Series<String, Double> cantidadDeCompraAnual = new XYChart.Series<>("Catidad de productos comprados", cantidadTotalAnual);
        XYChart.Series<String, Double> ganaciasAnuales = new XYChart.Series<>("Ganancias anuales", gananciasAnuales);

        // Agregar serie al gráfico
        productosComprados.getData().add(serieVentasAnulaes);
        cantidadDeVentas.getData().add(cantidadDeCompraAnual);
        dineroGenerado.getData().add(ganaciasAnuales);
    }

    public ObservableList<XYChart.Data<String, Integer>> recuperar_datos (){
        // Crear datos individuales
        XYChart.Data<String, Integer> enero = new XYChart.Data<>("Enero", 100);
        XYChart.Data<String, Integer> febrero = new XYChart.Data<>("Febrero", 200);
        XYChart.Data<String, Integer> marzo = new XYChart.Data<>("Marzo", 300);
        XYChart.Data<String, Integer> abril = new XYChart.Data<>("Abril", 400);
        XYChart.Data<String, Integer> mayo = new XYChart.Data<>("Mayo", 500);
        XYChart.Data<String, Integer> junio = new XYChart.Data<>("Junio", 600);
        XYChart.Data<String, Integer> julio = new XYChart.Data<>("Julio", 700);
        XYChart.Data<String, Integer> agosto = new XYChart.Data<>("Agosto", 800);
        XYChart.Data<String, Integer> septiembre = new XYChart.Data<>("Septiembre", 900);
        XYChart.Data<String, Integer> octubre = new XYChart.Data<>("Octubre", 1000);
        XYChart.Data<String, Integer> noviembre = new XYChart.Data<>("Noviembre", 1100);
        XYChart.Data<String, Integer> diciembre = new XYChart.Data<>("Diciembre", 1200);

        // Crear lista observable y agregar datos
        ObservableList<XYChart.Data<String, Integer>> datosVentas = FXCollections.observableArrayList(
                enero, febrero, marzo, abril, mayo, junio, julio, agosto, septiembre, octubre, noviembre, diciembre
        );
        return datosVentas;
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
            PantallaAdmin pantallaAdmin = this;
            controller.inicio(pantallaAdmin, tiposPago);

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
            PantallaAdmin pantallaAdmin = this;
            controller.inicio(pantallaAdmin, tiposPago, tablaUsuarios.getSelectionModel().getSelectedItem());


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
        usuariosfiltrados = observableArrayList();

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
