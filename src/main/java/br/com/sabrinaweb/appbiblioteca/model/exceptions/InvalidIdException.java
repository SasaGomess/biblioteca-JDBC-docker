package br.com.sabrinaweb.appbiblioteca.model.exceptions;

public class InvalidIdException extends NumberFormatException {
    public InvalidIdException(String message) {
        super(message);
    }
}
