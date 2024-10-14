package org.jaxtech.jaxtech.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jaxtech.jaxtech.HelloApplication;
import org.jaxtech.jaxtech.modelo.ImageTableCell;
import org.jaxtech.jaxtech.modelo.Producto;
import org.jaxtech.jaxtech.modelo.Usuario;

import java.io.IOException;

public class PantallaClientes {
    @FXML
    public Button buttAñadir;

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

    ObservableList<Producto> productosCarrito;

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
        productosCarrito = FXCollections.observableArrayList();
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
            if (producto.getStock() >= cantidad) {
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
            boolean encontrado = false;

            for (Producto productoCarrito : productosCarrito) {
                if (productoCarrito.getId() == producto.getId()) {
                    productoCarrito.setStock(productoCarrito.getStock() + cantidad);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                Producto productoCarrito = new Producto(producto);
                productoCarrito.setStock(cantidad);
                productosCarrito.add(productoCarrito);
            }


            if (producto.getStock() == 0) {
                productos.remove(producto);
            }

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
    void carrito() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/jaxtech/jaxtech/carritoClientes.fxml"));
            Parent root = fxmlLoader.load();
            CarritoClientes carritoClientes = fxmlLoader.getController();
            carritoClientes.setProductos(productosCarrito);
            carritoClientes.setUsuario(usuario);
            Scene nuevaScena = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(nuevaScena);
            stage.setTitle("Carrito");
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setScene(Scene scene) {
        // Cerramos la ventana
        ((Stage) scene.getWindow()).close();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void activarBoton() {
        buttAñadir.setDisable(false);
    }
}