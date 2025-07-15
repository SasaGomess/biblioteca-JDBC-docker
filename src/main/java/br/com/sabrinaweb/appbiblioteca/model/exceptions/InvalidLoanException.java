package br.com.sabrinaweb.appbiblioteca.model.exceptions;

public class InvalidLoanException extends RuntimeException {
    public InvalidLoanException(String message) {
        super(message);
    }
}
