package br.com.sabrinaweb.appbiblioteca.model.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String msg){
        super(msg);
    }
}
