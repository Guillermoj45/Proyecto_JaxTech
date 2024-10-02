package org.jaxtech.jaxtech.modelo;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.SimpleIntegerProperty;

public class Usuario {
    Integer id;
    String nombre;
    String apellidos;
    String direccion;
    String tipoPago;
    String numTelefono;
    Integer totalPedidos;

    public Usuario(int id, String nombre, String apellidos, String direccion, String tipoPago, String numTelefono, int totalPedidos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.tipoPago = tipoPago;
        this.numTelefono = numTelefono;
        this.totalPedidos = totalPedidos;
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
