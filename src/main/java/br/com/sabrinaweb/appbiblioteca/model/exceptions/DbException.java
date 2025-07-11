package br.com.sabrinaweb.appbiblioteca.conn.exeptions;

public class DbException extends RuntimeException {

    public DbException(String message) {
        super(message);
    }
}
