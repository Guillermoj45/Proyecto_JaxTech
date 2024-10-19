package org.jaxtech.jaxtech.modelo;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DDBB {
    // Variables para almacenar la URL, usuario y contraseña de la base de datos
    String url, user, password;
    // Variable estática para la conexión a la base de datos
    static Connection conexion;

    // Constructor de la clase DDBB
    public DDBB() {
        // Carga las variables de entorno desde un archivo .env
        Dotenv dotenv = Dotenv.load();
        // Asigna la URL, usuario y contraseña de la base de datos desde las variables de entorno
        this.url = "jdbc:mysql://" + dotenv.get("URL");
        this.user = dotenv.get("USER");
        this.password = dotenv.get("PASSWORD");
        // Establece la conexión a la base de datos
        conexion = conexion();
    }

    // Método estático para obtener la conexión a la base de datos
    public static Connection getConexion() {
        try {
            // Si la conexión está cerrada, crea una nueva instancia de DDBB
            if (conexion.isClosed())
                new DDBB();
            // Establece el auto-commit en verdadero
            conexion.setAutoCommit(true);
            // Retorna la conexión
            return conexion;
        } catch (SQLException e) {
            // Lanza una excepción en caso de error
            throw new RuntimeException(e);
        }
    }

    // Método para establecer la conexión a la base de datos
    public Connection conexion() {
        try {
            // Carga el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Retorna la conexión a la base de datos
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            // Lanza una excepción en caso de error
            throw new RuntimeException(e);
        }
    }
}