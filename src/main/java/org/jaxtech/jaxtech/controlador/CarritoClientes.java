package org.jaxtech.jaxtech.controlador;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Setter;
import org.jaxtech.jaxtech.modelo.ImageTableCell;
import org.jaxtech.jaxtech.modelo.Producto;
import org.jaxtech.jaxtech.modelo.Usuario;

/**
 * Controlador para gestionar el carrito de clientes.
 */
public class CarritoClientes {
    // Botón para eliminar un producto del carrito
    @FXML
    public Button buttElimiarProducto;

    // Tabla que muestra los productos en el carrito
    @FXML
    private TableView<Producto> Tabla;

    // Columnas de la tabla
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

    // Lista observable de productos en el carrito
    private ObservableList<Producto> productosCarrito;
    /**
     * -- SETTER --
     *  Método para establecer el usuario actual.
     *
     * @param usuario El usuario actual.
     */
    // Usuario actual
    @Setter
    private Usuario usuario;

    /**
     * Método de inicialización.
     * Configura las columnas de la tabla.
     */
    public void initialize() {
        // Configuración de las columnas de la tabla
        columNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        columTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        columCantidad.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        columCosteTotal.setCellValueFactory(cellData -> cellData.getValue().costeTotalProperty().asObject());
        columImagen.setCellValueFactory(cellData -> cellData.getValue().imagenProperty());
        columImagen.setCellFactory(column -> new ImageTableCell());
    }

    /**
     * Método para establecer los productos en el carrito.
     * @param productos Lista observable de productos.
     */
    public void setProductos(ObservableList<Producto> productos) {
        this.productosCarrito = productos;
        Tabla.setItems(productosCarrito);
    }

    /**
     * Método para eliminar un producto seleccionado del carrito.
     */
    @FXML
    void buttEliminarProducto() {
        Producto producto = Tabla.getSelectionModel().getSelectedItem();
        productosCarrito.remove(producto);
    }

    /**
     * Método para realizar la compra de los productos en el carrito.
     */
    @FXML
    void comprar() {
        Producto.comprados(productosCarrito, usuario);
        productosCarrito.clear();
        ((Stage) buttElimiarProducto.getScene().getWindow()).close();
    }

    /**
     * Método para habilitar el botón de eliminar producto.
     */
    public void habiliarBoton() {
        buttElimiarProducto.setDisable(false);
    }

}