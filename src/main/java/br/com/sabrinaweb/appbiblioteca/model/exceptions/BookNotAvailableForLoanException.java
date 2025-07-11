package br.com.sabrinaweb.appbiblioteca.model.exceptions;

public class BookNotAvailableForLoanException extends RuntimeException {
    public BookNotAvailableForLoanException(String message) {
        super(message);
    }
}
