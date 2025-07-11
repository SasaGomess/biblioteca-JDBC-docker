package br.com.sabrinaweb.appbiblioteca.model.dao;

import br.com.sabrinaweb.appbiblioteca.conn.ConnectionFactory;
import br.com.sabrinaweb.appbiblioteca.model.dao.impl.AuthorDaoJdbc;
import br.com.sabrinaweb.appbiblioteca.model.dao.impl.BookDaoJdbc;
import br.com.sabrinaweb.appbiblioteca.model.dao.impl.LibraryLoanDaoJdbc;
import br.com.sabrinaweb.appbiblioteca.model.dao.impl.UserDaoJdbc;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;

import java.sql.SQLException;

public class DaoFactory {
    public static UserDao createUserDao() {
        return new UserDaoJdbc(ConnectionFactory.getConnection());
    }
    public static BookDao createBookDao() {
        return new BookDaoJdbc(ConnectionFactory.getConnection());
    }
    public static AuthorDao createAuthorDao() {
        return new AuthorDaoJdbc(ConnectionFactory.getConnection());
    }

    public static LibraryLoanDao createLoanDao(){
        return new LibraryLoanDaoJdbc(ConnectionFactory.getConnection());
    }
}
