/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ni.com.casapellas.client.config;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Francisco Landeros
 */
public class ConnectionSqlite {

    private java.sql.Connection connection;
    private Statement statement;
    public String path;

    /**
     * Constructor for objects of class ConnectionSqlite
     */
    public ConnectionSqlite(String path) {
        this.path = path; //"C:/Databases/registro.db";
    }

    public void open() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
        
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            statement = getConnection().createStatement();
        } catch (SQLException e) {
        
        }
    }

    public void close() {
        try {
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public java.sql.Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }
}
