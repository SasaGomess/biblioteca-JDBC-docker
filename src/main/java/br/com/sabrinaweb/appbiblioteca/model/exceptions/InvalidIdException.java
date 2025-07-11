package br.com.sabrinaweb.appbiblioteca.model.exceptions;

public class InvalidIdException extends RuntimeException {
    public InvalidIdException(String message) {
        super(message);
    }
}
