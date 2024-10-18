package org.jaxtech.jaxtech.modelo;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.binding.IntegerExpression;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class DatosAnalisis {
    private String nombre;
    private Integer nVentas;
    private Integer uVendidas;
    private Double ganancias;

    public DatosAnalisis(String nombre, Integer nVentas, Integer uVendidas, Double ganancias) {
        this.nombre = nombre;
        this.nVentas = nVentas;
        this.uVendidas = uVendidas;
        this.ganancias = ganancias;
    }
    
    public static ObservableList<XYChart.Data<String, Double>> cantidadComprasAnuales() {
        String sql = """
                SELECT
                    MONTHNAME(DATE_ADD(CURDATE(), INTERVAL - (n.n - 1) MONTH)) AS mes,
                    COUNT(p.fecha) AS pedidos_12_meses
                FROM
                    (SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
                     UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
                     UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12) AS n
                        LEFT JOIN
                    pedidos p ON MONTH(p.fecha) = MONTH(DATE_ADD(CURDATE(), INTERVAL - (n.n - 1) MONTH))
                        AND YEAR(p.fecha) = YEAR(CURDATE())
                GROUP BY
                    n.n
                ORDER BY
                    n.n;
                """;
        Connection conexion = DDBB.getConexion();
        try {
            ResultSet resultado = conexion.prepareStatement(sql).executeQuery();
            ObservableList<XYChart.Data<String, Double>> lista = FXCollections.observableArrayList();

            while (resultado.next()) {
                String mes = resultado.getString("mes");
                Double pedidos = resultado.getDouble("pedidos_12_meses");
                XYChart.Data<String, Double> data = new XYChart.Data<>(mes, pedidos);
                lista.add(data);
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<XYChart.Data<String, Double>> cantidadDeProductosComprados() {
        String sql = """
                SELECT
                     MONTHNAME(DATE_ADD(CURDATE(), INTERVAL - (n.n - 1) MONTH)) AS mes,
                     Sum(m.cantidad) AS cantidad
                FROM
                     (SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
                      UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
                      UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12) AS n
                         LEFT JOIN
                     pedidos p ON MONTH(p.fecha) = MONTH(DATE_ADD(CURDATE(), INTERVAL - (n.n - 1) MONTH))
                         AND YEAR(p.fecha) = YEAR(CURDATE())
                            LEFT JOIN JaxTech.multipedidos m on p.id = m.id_pedido
                GROUP BY
                     n.n
                ORDER BY
                     n.n;
                """;
        Connection conexion = DDBB.getConexion();
        try {
            ResultSet resultado = conexion.prepareStatement(sql).executeQuery();
            ObservableList<XYChart.Data<String, Double>> lista = FXCollections.observableArrayList();

            while (resultado.next()) {
                String mes = resultado.getString("mes");
                Double pedidos = resultado.getDouble("cantidad");
                XYChart.Data<String, Double> data = new XYChart.Data<>(mes, pedidos);

                lista.add(data);
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<XYChart.Data<String, Double>> gananciasAnuales (){
        String sql = """
                SELECT
                    MONTHNAME(DATE_ADD(CURDATE(), INTERVAL - (n.n - 1) MONTH)) AS mes,
                    SUM(m.cantidad * m.precio) AS ganancias
                FROM
                    (SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
                     UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
                     UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12) AS n
                        LEFT JOIN
                    pedidos p ON MONTH(p.fecha) = MONTH(DATE_ADD(CURDATE(), INTERVAL - (n.n - 1) MONTH))
                        AND YEAR(p.fecha) = YEAR(CURDATE())
                        LEFT JOIN multipedidos m ON p.id = m.id_pedido
                GROUP BY
                    n.n
                ORDER BY
                    n.n;
                """;
        Connection conexion = DDBB.getConexion();
        try {
            ResultSet resultado = conexion.prepareStatement(sql).executeQuery();
            ObservableList<XYChart.Data<String, Double>> lista = FXCollections.observableArrayList();

            while (resultado.next()) {
                String mes = resultado.getString("mes");
                Double ganancias = resultado.getDouble("ganancias");
                XYChart.Data<String, Double> data = new XYChart.Data<>(mes, ganancias);
                lista.add(data);
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public ObservableList<DatosAnalisis> datosTabla() {
        ObservableList<DatosAnalisis> lista = FXCollections.observableArrayList();
        String sql = """
                SELECT
                    p.nombre,
                    COUNT(p.id) AS nVentas,
                    SUM(m.cantidad) AS uVendidas,
                    SUM(m.cantidad * m.precio) AS ganancias
                FROM
                    productos p
                        LEFT JOIN
                    multipedidos m ON p.id = m.id_producto
                GROUP BY
                    p.id
                ORDER BY
                    nVentas DESC
                    LIMIT 5;
                """;
        Connection conexion = DDBB.getConexion();
        try {
            ResultSet resultado = conexion.prepareStatement(sql).executeQuery();
            while (resultado.next()) {
                String nombre = resultado.getString("nombre");
                Integer nVentas = resultado.getInt("nVentas");
                Integer uVendidas = resultado.getInt("uVendidas");
                Double ganancias = resultado.getDouble("ganancias");
                DatosAnalisis datos = new DatosAnalisis(nombre, nVentas, uVendidas, ganancias);
                lista.add(datos);
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableValue<String> nombreProperty() {
        return new SimpleStringProperty(nombre);
    }

    public IntegerExpression nVentasProperty() {
        return new SimpleIntegerProperty(nVentas);
    }

    public IntegerExpression uVendidasProperty() {
        return new SimpleIntegerProperty(uVendidas);
    }

    public DoubleExpression gananciasProperty() {
        return new SimpleDoubleProperty(ganancias);
    }
}
