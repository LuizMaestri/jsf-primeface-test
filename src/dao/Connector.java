package dao;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author luiz
 * @version 1
 * @since 03/05/16
 */
public class Connector {

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        DriverManager.registerDriver(new Driver());
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/Neoway","postgres", "postgres");
    }
}
