package br.com.sabrinaweb.appbiblioteca.model.exceptions;

public class DbException extends RuntimeException {

    public DbException(String message) {
        super(message);
    }
}
