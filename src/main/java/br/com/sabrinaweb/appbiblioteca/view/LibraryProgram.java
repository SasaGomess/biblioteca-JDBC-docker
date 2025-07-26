package br.com.sabrinaweb.appbiblioteca.view;

import br.com.sabrinaweb.appbiblioteca.model.dao.DaoFactory;
import br.com.sabrinaweb.appbiblioteca.model.service.AuthorService;
import br.com.sabrinaweb.appbiblioteca.model.service.BookService;
import br.com.sabrinaweb.appbiblioteca.model.service.LibraryLoanService;
import br.com.sabrinaweb.appbiblioteca.model.service.UserService;
import lombok.extern.log4j.Log4j2;

import java.util.Scanner;

@Log4j2
public class LibraryProgram {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("+.°.+.°. Welcome to our library! +.+°.°+.");
        MenuMain menu = new MenuMain(new BookService(SCANNER, DaoFactory.createBookDao()), new AuthorService(SCANNER, DaoFactory.createAuthorDao()), new LibraryLoanService(SCANNER, DaoFactory.createLoanDao()), new UserService(SCANNER, DaoFactory.createUserDao()), SCANNER);
        menu.menu();
    }
}