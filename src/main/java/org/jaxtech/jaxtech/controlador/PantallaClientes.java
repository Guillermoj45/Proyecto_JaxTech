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
import lombok.Setter;
import org.jaxtech.jaxtech.HelloApplication;
import org.jaxtech.jaxtech.modelo.ImageTableCell;
import org.jaxtech.jaxtech.modelo.Producto;
import org.jaxtech.jaxtech.modelo.Usuario;

import java.io.IOException;

/**
 * Controlador para la pantalla de clientes.
 */
public class PantallaClientes {
    @FXML
    public Button buttAgregar; // Botón para agregar productos al carrito

    @FXML
    private TableColumn<Producto, Float> columnTablaPrecio; // Columna para indicar si el producto es apto para gaming

    @FXML
    private TableColumn<Producto, Image> columTablaProductosImagene; // Columna para mostrar la imagen del producto

    @FXML
    private TableColumn<Producto, String> columTablaProductosNombre; // Columna para mostrar el nombre del producto

    @FXML
    private TableColumn<Producto, Integer> columTablaProductosStock; // Columna para mostrar el stock del producto

    @FXML
    private TableColumn<Producto, String> columTablaProductosTipo; // Columna para mostrar el tipo del producto

    @FXML
    private TableView<Producto> tablaProductos; // Tabla para mostrar los productos

    @Setter
    Usuario usuario; // Usuario actual
    ObservableList<Producto> productos; // Lista de productos disponibles

    ObservableList<Producto> productosCarrito; // Lista de productos en el carrito

    @FXML
    public void initialize() {
        // Inicializa las columnas de la tabla con las propiedades de los productos
        columTablaProductosNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        columTablaProductosTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        columTablaProductosStock.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        columnTablaPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());
        columTablaProductosImagene.setCellValueFactory(cellData -> cellData.getValue().imagenProperty());
        columTablaProductosImagene.setCellFactory(column -> new ImageTableCell());

        // Obtiene la lista de productos y la asigna a la tabla
        productos = Producto.getProductos();
        tablaProductos.setItems(productos);
        productosCarrito = FXCollections.observableArrayList();
    }

    @FXML
    void agregar_producto() {
        // Agrega el producto seleccionado al carrito
        Producto producto = tablaProductos.getSelectionModel().getSelectedItem();
        if (producto != null) {
            int cantidad;
            try {
                cantidad = getCantidad();
                if (cantidad <= 0) {
                    return;
                }
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
        // Muestra un diálogo para ingresar la cantidad de productos
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cantidad");
        dialog.setHeaderText("Cantidad de productos");
        dialog.setContentText("Cantidad:");
        dialog.showAndWait();

        return Integer.parseInt(dialog.getResult());
    }

    @FXML
    void carrito() {
        // Abre la ventana del carrito de compras
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/jaxtech/jaxtech/carritoClientes.fxml"));
            Parent root = fxmlLoader.load();
            CarritoClientes carritoClientes = fxmlLoader.getController();
            carritoClientes.setProductos(productosCarrito, productos, this);
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
        // Cierra la ventana actual
        ((Stage) scene.getWindow()).close();
    }

    public void activarBoton() {
        // Activa el botón de agregar producto
        buttAgregar.setDisable(false);
    }

    public void actualizarTabla() {
        tablaProductos.refresh();
    }
}