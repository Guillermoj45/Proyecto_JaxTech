package org.jaxtech.jaxtech.modelo;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Producto {
    private int id;
    private String nombre;
    private String tipoPago;
    private int stock;
    private float precio;
    private Image imagen;
    private Boolean aptoGaming;

    public Producto(int id, String nombre, String tipoPago, int stock, float precio, Image imagen, boolean aptoGaming) {
        this.id = id;
        this.nombre = nombre;
        this.tipoPago = tipoPago;
        this.stock = stock;
        this.precio = precio;
        this.imagen = imagen;
        this.aptoGaming = aptoGaming;
    }

    public Producto(String nombre, String tipoPago, int stock, float precio, Image imagen, boolean aptoGaming) {
        this.nombre = nombre;
        this.tipoPago = tipoPago;
        this.stock = stock;
        this.precio = precio;
        this.imagen = imagen;
        this.aptoGaming = aptoGaming;
    }

    public boolean insert() {
        Connection conexion = DDBB.getConexion();
        String[] extensiones = imagen.getUrl().split("\\.");
        String extension = extensiones[extensiones.length - 1];
        byte[] imageBytes;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(SwingFXUtils.fromFXImage(imagen, null), extension, baos);
            imageBytes = baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            PreparedStatement insert = conexion.prepareStatement("INSERT INTO productos (nombre, tipo, stock, precio, imagen, aptoParaGaming) VALUES (?, ?, ?, ?, ?, ?)");
            insert.setString(1, getNombre());
            insert.setString(2, getTipoPago());
            insert.setInt(3, getStock());
            insert.setFloat(4, getPrecio());
            insert.setBytes(5, imageBytes);
            insert.setBoolean(6, isAptoGaming());
            insert.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ObservableList<Producto> getProductos() {
        Connection conexion = DDBB.getConexion();
        ObservableList<Producto> productos = FXCollections.observableArrayList();
        String sql = "SELECT * FROM productos where stock > 0";
        try {
            PreparedStatement select = conexion.prepareStatement(sql);
            ResultSet resultado = select.executeQuery();

            while (resultado.next()){
                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                String tipo = resultado.getString("tipo");
                int stock = resultado.getInt("stock");
                float precio = resultado.getFloat("precio");
                byte[] imagenBytes = resultado.getBytes("imagen");
                ByteArrayInputStream bais = new ByteArrayInputStream(imagenBytes);
                Image imagen = new Image(bais);
                boolean aptoParaGaming = resultado.getBoolean("aptoParaGaming");

                Producto producto = new Producto(id, nombre, tipo, stock, precio, imagen, aptoParaGaming);
                productos.add(producto);
            }
            return productos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }

    public boolean isAptoGaming() {
        return aptoGaming;
    }

    public void setAptoGaming(boolean aptoGaming) {
        this.aptoGaming = aptoGaming;
    }

    public ObservableValue<String> nombreProperty() {
        return new SimpleStringProperty(nombre);
    }

    public ObservableValue<String> tipoProperty() {
        return new SimpleStringProperty(tipoPago);
    }

    public SimpleIntegerProperty stockProperty() {
        return new SimpleIntegerProperty(stock);
    }


    public ObservableValue<Image> imagenProperty() {
        return new SimpleObjectProperty<>(imagen);
    }


    public ObservableValue<Boolean> aptoGamingProperty() {
        return new SimpleBooleanProperty(aptoGaming);
    }
}
