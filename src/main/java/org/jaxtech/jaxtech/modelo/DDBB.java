package org.jaxtech.jaxtech.modelo;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DDBB {
    String url, user, password;
    static Connection conexion;

    public DDBB(){
        Dotenv dotenv = Dotenv.load();
        this.url = "jdbc:mysql://" + dotenv.get("URL");
        this.user = dotenv.get("USER");
        this.password = dotenv.get("PASSWORD");
        conexion = conexion();
    }

    public static Connection getConexion(){
        try {
            if (conexion.isClosed())
                new DDBB();
            conexion.setAutoCommit(true);
            return conexion;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Connection conexion(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
