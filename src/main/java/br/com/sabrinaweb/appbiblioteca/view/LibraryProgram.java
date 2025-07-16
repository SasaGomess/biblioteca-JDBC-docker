package br.com.sabrinaweb.appbiblioteca.view;

import br.com.sabrinaweb.appbiblioteca.model.service.AuthorService;
import br.com.sabrinaweb.appbiblioteca.model.service.BookService;
import br.com.sabrinaweb.appbiblioteca.model.service.LibraryLoanService;
import br.com.sabrinaweb.appbiblioteca.model.service.UserService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LibraryProgram {
    public static void main(String[] args) {
        System.out.println("+.°.+.°. Welcome to our library! +.+°.°+.");
        MenuMain menu = new MenuMain(new BookService(), new AuthorService(), new LibraryLoanService(), new UserService());
        menu.menu();
    }
}