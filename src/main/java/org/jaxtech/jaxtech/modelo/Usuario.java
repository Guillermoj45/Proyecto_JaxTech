package org.jaxtech.jaxtech.modelo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase Usuario que representa un usuario en la base de datos.
 */
@Getter
@Setter
public class Usuario {
    Integer id;
    String nombre;
    String apellidos;
    String direccion;
    String tipoPago;
    String numTelefono;
    Integer totalPedidos;
    String password;
    boolean admin;

    /**
     * Constructor que inicializa todos los atributos del usuario.
     * @param id El ID del usuario.
     * @param nombre El nombre del usuario.
     * @param apellidos Los apellidos del usuario.
     * @param direccion La dirección del usuario.
     * @param tipoPago El tipo de pago del usuario.
     * @param numTelefono El número de teléfono del usuario.
     * @param totalPedidos El total de pedidos del usuario.
     * @param password La contraseña del usuario.
     * @param admin Indica si el usuario es administrador.
     */
    public Usuario(int id, String nombre, String apellidos, String direccion, String tipoPago, String numTelefono, int totalPedidos, String password, boolean admin) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.tipoPago = tipoPago;
        this.numTelefono = numTelefono;
        this.totalPedidos = totalPedidos;
        this.password = password;
        this.admin = admin;
    }

    /**
     * Constructor que inicializa todos los atributos del usuario excepto el ID.
     * @param nombre El nombre del usuario.
     * @param apellidos Los apellidos del usuario.
     * @param direccion La dirección del usuario.
     * @param tipoPago El tipo de pago del usuario.
     * @param numTelefono El número de teléfono del usuario.
     * @param totalPedidos El total de pedidos del usuario.
     * @param password La contraseña del usuario.
     * @param admin Indica si el usuario es administrador.
     */
    public Usuario(String nombre, String apellidos, String direccion, String tipoPago, String numTelefono, int totalPedidos, String password, boolean admin) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.tipoPago = tipoPago;
        this.numTelefono = numTelefono;
        this.totalPedidos = totalPedidos;
        this.password = password;
        this.admin = admin;
    }

    /**
     * Constructor que inicializa todos los atributos del usuario excepto la contraseña.
     * @param id El ID del usuario.
     * @param nombre El nombre del usuario.
     * @param apellidos Los apellidos del usuario.
     * @param direccion La dirección del usuario.
     * @param pago El tipo de pago del usuario.
     * @param telefono El número de teléfono del usuario.
     * @param totalPedidos El total de pedidos del usuario.
     * @param admin Indica si el usuario es administrador.
     */
    public Usuario(int id, String nombre, String apellidos, String direccion, String pago, String telefono, int totalPedidos, boolean admin) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.tipoPago = pago;
        this.numTelefono = telefono;
        this.totalPedidos = totalPedidos;
        this.admin = admin;
    }

    /**
     * Constructor que inicializa todos los atributos del usuario excepto la contraseña y el total de pedidos.
     * @param id El ID del usuario.
     * @param nombreUsuario El nombre del usuario.
     * @param apellidos Los apellidos del usuario.
     * @param direccion La dirección del usuario.
     * @param pago El tipo de pago del usuario.
     * @param telefono El número de teléfono del usuario.
     * @param admin Indica si el usuario es administrador.
     */
    public Usuario(int id, String nombreUsuario, String apellidos, String direccion, String pago, String telefono, boolean admin) {
        this.id = id;
        this.nombre = nombreUsuario;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.tipoPago = pago;
        this.numTelefono = telefono;
        this.admin = admin;
    }

    /**
     * Método estático para obtener una lista observable de usuarios desde la base de datos.
     * @return Una lista observable de usuarios.
     */
    public static ObservableList<Usuario> getUsuarios() {
        String sql = """
                                SELECT u.id, u.nombre, u.apellidos, u.direccion, u.pago, u.telefono,
                                        COUNT(p.id) AS Pedidos, u.admin
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
                        resultado.getInt(7),
                        resultado.getBoolean(8)
                );
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método para insertar el usuario en la base de datos.
     * @return true si el usuario se insertó correctamente, false en caso contrario.
     */
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

    /**
     * Método para actualizar el usuario en la base de datos.
     */
    public void update() {
        if (this.password == null || this.password.isEmpty()) {
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

    /**
     * Método para eliminar el usuario de la base de datos.
     */
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

    /**
     * Método para obtener una propiedad observable del ID del usuario.
     * @return Una propiedad observable del ID del usuario.
     */
    public SimpleIntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    /**
     * Método para obtener una propiedad observable del total de pedidos del usuario.
     * @return Una propiedad observable del total de pedidos del usuario.
     */
    public SimpleIntegerProperty totalPedidosProperty() {
        return new SimpleIntegerProperty(totalPedidos);
    }

    /**
     * Método para obtener una propiedad observable del nombre del usuario.
     * @return Una propiedad observable del nombre del usuario.
     */
    public javafx.beans.property.SimpleStringProperty nombreProperty() {
        return new javafx.beans.property.SimpleStringProperty(nombre);
    }

    /**
     * Método para obtener una propiedad observable de los apellidos del usuario.
     * @return Una propiedad observable de los apellidos del usuario.
     */
    public javafx.beans.property.SimpleStringProperty apellidosProperty() {
        return new javafx.beans.property.SimpleStringProperty(apellidos);
    }

    /**
     * Método para obtener una propiedad observable de la dirección del usuario.
     * @return Una propiedad observable de la dirección del usuario.
     */
    public javafx.beans.property.SimpleStringProperty direccionProperty() {
        return new javafx.beans.property.SimpleStringProperty(direccion);
    }

    /**
     * Método para obtener una propiedad observable del tipo de pago del usuario.
     * @return Una propiedad observable del tipo de pago del usuario.
     */
    public javafx.beans.property.SimpleStringProperty tipoPagoProperty() {
        return new javafx.beans.property.SimpleStringProperty(tipoPago);
    }

    /**
     * Método para obtener una propiedad observable del número de teléfono del usuario.
     * @return Una propiedad observable del número de teléfono del usuario.
     */
    public javafx.beans.property.SimpleStringProperty numTelefonoProperty() {
        return new javafx.beans.property.SimpleStringProperty(numTelefono);
    }

}