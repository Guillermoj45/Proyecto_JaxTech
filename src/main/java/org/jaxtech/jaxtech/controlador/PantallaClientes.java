package org.jaxtech.jaxtech.controlador;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jaxtech.jaxtech.modelo.ImageTableCell;
import org.jaxtech.jaxtech.modelo.Producto;
import org.jaxtech.jaxtech.modelo.Usuario;
import java.util.ArrayList;

public class PantallaClientes {

    @FXML
    private TableColumn<Producto, Boolean> columTablaProductosApto;

    @FXML
    private TableColumn<Producto, Image> columTablaProductosImagene;

    @FXML
    private TableColumn<Producto, String> columTablaProductosNombre;

    @FXML
    private TableColumn<Producto, Integer> columTablaProductosStock;

    @FXML
    private TableColumn<Producto, String> columTablaProductosTipo;

    @FXML
    private TableView<Producto> tablaProductos;

    Usuario usuario;
    ObservableList<Producto> productos;

    ArrayList<Producto> productosCarrito;

    @FXML
    public void initialize() {
        columTablaProductosNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        columTablaProductosTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        columTablaProductosStock.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        columTablaProductosApto.setCellValueFactory(cellData -> cellData.getValue().aptoGamingProperty());
        columTablaProductosImagene.setCellValueFactory(cellData -> cellData.getValue().imagenProperty());
        columTablaProductosImagene.setCellFactory(column -> new ImageTableCell());

        productos = Producto.getProductos();
        tablaProductos.setItems(productos);
        productosCarrito = new ArrayList<>();
    }

    @FXML
    void agregar_producto() {
        Producto producto = tablaProductos.getSelectionModel().getSelectedItem();
        if (producto != null) {
            int cantidad;
            try {
                 cantidad = getCantidad();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cantidad no válida");
                alert.setContentText("La cantidad ingresada no es válida");
                alert.showAndWait();
                return;
            }
            if (producto.getStock() >= cantidad){
                producto.setStock(producto.getStock() - cantidad);
                tablaProductos.refresh();
                System.out.println("Producto: " + producto.getNombre() + " Cantidad: " + cantidad + "stock: " + producto.getStock());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No hay suficiente stock");
                alert.setContentText("No hay suficiente stock para el producto seleccionado");
                alert.showAndWait();
                return;
            }

            boolean productoEnCarrito = false;
            for (Producto p : productosCarrito) {
                if (p.getId() == producto.getId()) {
                    p.setStock(p.getStock() + cantidad);
                    productoEnCarrito = true;
                    break;
                }
            }

            if (!productoEnCarrito) {
                Producto productoCarrito = new Producto(producto);
                productoCarrito.setStock(cantidad);
                productosCarrito.add(productoCarrito);
            }
            if (producto.getStock() == 0) {
                productos.remove(producto);
            }
            productosCarrito.add(producto);
            System.out.println("Producto agregado al carrito: " + producto.getNombre());
        }

    }

    private int getCantidad() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cantidad");
        dialog.setHeaderText("Cantidad de productos");
        dialog.setContentText("Cantidad:");
        dialog.showAndWait();

        return Integer.parseInt(dialog.getResult());
    }

    @FXML
    void carrito(ActionEvent event) {
        System.out.println("Carrito");
    }

    public void setScene(Scene scene) {
        // Cerramos la ventana
        ((Stage) scene.getWindow()).close();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}