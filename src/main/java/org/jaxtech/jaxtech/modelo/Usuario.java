package org.jaxtech.jaxtech.modelo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario {
    Integer id;
    String nombre;
    String apellidos;
    String direccion;
    String tipoPago;
    String numTelefono;
    Integer totalPedidos;
    String password;

    public Usuario(int id, String nombre, String apellidos, String direccion, String tipoPago, String numTelefono, int totalPedidos, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.tipoPago = tipoPago;
        this.numTelefono = numTelefono;
        this.totalPedidos = totalPedidos;
        this.password = password;
    }

    public Usuario(String nombre, String apellidos, String direccion, String tipoPago, String numTelefono, int totalPedidos, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.tipoPago = tipoPago;
        this.numTelefono = numTelefono;
        this.totalPedidos = totalPedidos;
        this.password = password;
    }

    public Usuario(int id, String nombre, String apellidos, String direccion, String pago, String telefono, int totalPedidos){
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.tipoPago = pago;
        this.numTelefono = telefono;
        this.totalPedidos = totalPedidos;
    }

    public static ObservableList<Usuario> getUsuarios() {
        String sql = """
                SELECT u.id, u.nombre, u.apellidos, u.direccion, u.pago, u.telefono,
                        COUNT(p.id) AS Pedidos
                FROM usuarios u
                    LEFT JOIN pedidos p ON u.id = p.id_usuario
                where eliminado = false
                GROUP BY u.id, u.nombre, u.apellidos, u.direccion, u.pago, u.telefono;
""";
        try (Connection conexion = DDBB.getConexion();
             PreparedStatement select = conexion.prepareStatement(sql);
             ResultSet resultado = select.executeQuery()) {
            ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
            while (resultado.next()) {
                Usuario usuario = new Usuario(
                        resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4),
                        resultado.getString(5),
                        resultado.getString(6),
                        resultado.getInt(7)
                );
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insert() {
        String sql = "INSERT INTO usuarios (nombre, apellidos, direccion, pago, telefono, password) VALUES (?, ?, ?, ?, ?, password(?))";

        try {
            Connection conexion = DDBB.getConexion();
            if (conexion.isClosed()) {
                System.out.println("Conexión cerrada, abriendo nueva conexión");
                new DDBB();
            }
            PreparedStatement insert = conexion.prepareStatement(sql);
            insert.setString(1, this.getNombre());
            insert.setString(2, this.getApellidos());
            insert.setString(3, this.getDireccion());
            insert.setString(4, this.getTipoPago());
            insert.setString(5, this.getNumTelefono());
            insert.setString(6, this.getPassword());
            insert.execute();

            PreparedStatement select = conexion.prepareStatement("select id from usuarios where telefono = ? limit 1");
            select.setString(1, this.getNumTelefono());
            select.executeQuery();
            ResultSet resultado = select.executeQuery();
            resultado.next();
            setId(resultado.getInt(1));
            System.out.print("Usuario insertado correctamente");
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        if (this.password == null || this.password.isEmpty()){
            String sql = "UPDATE usuarios SET nombre = ?, apellidos = ?, direccion = ?, pago = ?, telefono = ? WHERE id = ?";
            try (Connection conexion = DDBB.getConexion();
                 PreparedStatement update = conexion.prepareStatement(sql)) {
                update.setString(1, this.getNombre());
                update.setString(2, this.getApellidos());
                update.setString(3, this.getDireccion());
                update.setString(4, this.getTipoPago());
                update.setString(5, this.getNumTelefono());
                update.setInt(6, this.getId());
                update.execute();
                System.out.print("Usuario actualizado correctamente");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        String sql = "UPDATE usuarios SET nombre = ?, apellidos = ?, direccion = ?, pago = ?, telefono = ?, password = password(?) WHERE id = ?";
        try (Connection conexion = DDBB.getConexion();
             PreparedStatement update = conexion.prepareStatement(sql)) {
            update.setString(1, this.getNombre());
            update.setString(2, this.getApellidos());
            update.setString(3, this.getDireccion());
            update.setString(4, this.getTipoPago());
            update.setString(5, this.getNumTelefono());
            update.setString(6, this.getPassword());
            update.setInt(7, this.getId());
            update.execute();
            System.out.print("Usuario actualizado correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        String sql = "update usuarios set eliminado = true where id = ?";
        Connection conexion = DDBB.getConexion();
        try {
            PreparedStatement delete = conexion.prepareStatement(sql);
            delete.setInt(1, this.getId());
            delete.executeUpdate();
            System.out.print("Usuario eliminado correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTotalPedidos(Integer totalPedidos) {
        this.totalPedidos = totalPedidos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }

    public int getTotalPedidos() {
        return totalPedidos;
    }

    public void setTotalPedidos(int totalPedidos) {
        this.totalPedidos = totalPedidos;
    }

    public SimpleIntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public SimpleIntegerProperty totalPedidosProperty() {
        return new SimpleIntegerProperty(totalPedidos);
    }

    public javafx.beans.property.SimpleStringProperty nombreProperty() {
        return new javafx.beans.property.SimpleStringProperty(nombre);
    }

    public javafx.beans.property.SimpleStringProperty apellidosProperty() {
        return new javafx.beans.property.SimpleStringProperty(apellidos);
    }

    public javafx.beans.property.SimpleStringProperty direccionProperty() {
        return new javafx.beans.property.SimpleStringProperty(direccion);
    }

    public javafx.beans.property.SimpleStringProperty tipoPagoProperty() {
        return new javafx.beans.property.SimpleStringProperty(tipoPago);
    }

    public javafx.beans.property.SimpleStringProperty numTelefonoProperty() {
        return new javafx.beans.property.SimpleStringProperty(numTelefono);
    }

}
