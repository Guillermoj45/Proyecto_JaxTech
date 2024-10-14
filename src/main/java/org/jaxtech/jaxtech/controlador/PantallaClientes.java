package org.jaxtech.jaxtech.controlador;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jaxtech.jaxtech.modelo.ImageTableCell;
import org.jaxtech.jaxtech.modelo.Producto;

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

    @FXML
    public void initialize() {
        columTablaProductosNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        columTablaProductosTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        columTablaProductosStock.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        columTablaProductosApto.setCellValueFactory(cellData -> cellData.getValue().aptoGamingProperty());
        columTablaProductosImagene.setCellValueFactory(cellData -> cellData.getValue().imagenProperty());
        columTablaProductosImagene.setCellFactory(column -> new ImageTableCell());

        ObservableList<Producto> productos = Producto.getProductos();
        tablaProductos.setItems(productos);

    }

    @FXML
    void agregar_producto(ActionEvent event) {

    }

    @FXML
    void carrito(ActionEvent event) {
        System.out.println("Carrito");
    }

    public void setScene(Scene scene) {
        // Cerramos la ventana
        ((Stage) scene.getWindow()).close();
    }

}