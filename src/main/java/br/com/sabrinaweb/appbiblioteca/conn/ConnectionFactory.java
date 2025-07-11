package br.com.sabrinaweb.appbiblioteca.conn;

import br.com.sabrinaweb.appbiblioteca.model.exceptions.DbException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null){
            try {
                String url = "jdbc:mysql://localhost:3306/library";
                String user = "root";
                String password = "devroot";
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return conn;
    }
}
