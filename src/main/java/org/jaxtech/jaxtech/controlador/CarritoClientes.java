package org.jaxtech.jaxtech.controlador;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jaxtech.jaxtech.modelo.ImageTableCell;
import org.jaxtech.jaxtech.modelo.Producto;
import org.jaxtech.jaxtech.modelo.Usuario;


public class CarritoClientes {

    public Button buttElimiarProducto;
    @FXML
    private TableView<Producto> Tabla;

    @FXML
    private TableColumn<Producto, Integer> columCantidad;

    @FXML
    private TableColumn<Producto, Integer> columCosteTotal;

    @FXML
    private TableColumn<Producto, Image> columImagen;

    @FXML
    private TableColumn<Producto, String> columNombre;

    @FXML
    private TableColumn<Producto, String> columTipo;

    private ObservableList<Producto> productosCarrito;
    private Usuario usuario;


    public void initialize() {
        columNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        columTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        columCantidad.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        columCosteTotal.setCellValueFactory(cellData -> cellData.getValue().costeTotalProperty().asObject());
        columImagen.setCellValueFactory(cellData -> cellData.getValue().imagenProperty());
        columImagen.setCellFactory(column -> new ImageTableCell());
    }

    public void setProductos(ObservableList<Producto> productos) {
        this.productosCarrito = productos;
        Tabla.setItems(productosCarrito);
    }


    @FXML
    void buttEliminarProducto() {
        Producto producto = Tabla.getSelectionModel().getSelectedItem();
        productosCarrito.remove(producto);
    }

    @FXML
    void comprar() {
        Producto.comprados(productosCarrito, usuario);
        productosCarrito.clear();
        ((Stage) buttElimiarProducto.getScene().getWindow()).close();
    }

    public void habiliarBoton() {
        buttElimiarProducto.setDisable(false);
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
