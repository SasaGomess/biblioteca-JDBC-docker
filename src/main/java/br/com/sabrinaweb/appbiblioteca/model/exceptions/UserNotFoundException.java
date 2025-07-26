package br.com.sabrinaweb.appbiblioteca.model.exceptions;

public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException(String msg){
        super(msg);
    }
}
