package br.com.sabrinaweb.appbiblioteca.model.dao;

import br.com.sabrinaweb.appbiblioteca.conn.ConnectionFactory;
import br.com.sabrinaweb.appbiblioteca.model.dao.impl.UserDaoJdbc;

import java.sql.SQLException;

public class DaoFactory {
    public static UserDao createUser() {
        return new UserDaoJdbc(ConnectionFactory.getConnection());
    }
}
