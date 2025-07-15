package br.com.sabrinaweb.appbiblioteca.view;

import br.com.sabrinaweb.appbiblioteca.service.AuthorService;
import br.com.sabrinaweb.appbiblioteca.service.BookService;
import br.com.sabrinaweb.appbiblioteca.service.LibraryLoanService;
import br.com.sabrinaweb.appbiblioteca.service.UserService;
import lombok.extern.log4j.Log4j2;

import java.util.Scanner;

@Log4j2
public class MenuMain {
    private BookService bookService;
    private AuthorService authorService;
    private LibraryLoanService libraryLoanService;
    private UserService userService;

    private static final Scanner SCANNER = new Scanner(System.in);

    public MenuMain(BookService bookService, AuthorService authorService, LibraryLoanService libraryLoanService, UserService userService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.libraryLoanService = libraryLoanService;
        this.userService = userService;
    }
    public MenuMain() {
    }
    public void menu(){
        try {
            showMenuOptions();
            int menuResp = Integer.parseInt(SCANNER.nextLine());
            do {
                switch (menuResp){
                    case 1 -> userMenu();
                    case 2 -> bookMenu();
                    case 3 -> loanMenu();
                    case 4 -> authorMenu();
                    case 5 -> reportMenu();
                    case 0 -> System.out.println("Exiting...");
                    default -> throw new IllegalArgumentException("Invalid option you must enter the available options :)");
                }
                if (menuResp != 0){
                    showMenuOptions();
                    menuResp = Integer.parseInt(SCANNER.nextLine());
                }
            }while (menuResp != 0);
        }catch (IllegalArgumentException e){
            log.error(e);
        }
    }
    public void showMenuOptions(){
        System.out.println("--+--Choose a operation--+--");
        System.out.println("1 - User");
        System.out.println("2 - Book");
        System.out.println("3 - Loan Service");
        System.out.println("4 - Author");
        System.out.println("5 - Generate reports");
        System.out.println("0 - >> EXIT");
        System.out.print("RESP: ");
    }
    public void bookMenu(){
        System.out.println("1 - Insert new book");
        System.out.println("2 - Update a book");
        System.out.println("3 - Delete a book");
        System.out.println("4 - Find book by title");
        System.out.println("5 - Find available books");
        System.out.println("6 - Find book(s) of a author");
        System.out.println("9 - >> Return to the main menu");

        System.out.print("RESP: ");
        int resp = Integer.parseInt(SCANNER.nextLine());

        switch (resp){
            case 1 -> bookService.insert();
            case 2 -> bookService.update();
            case 3 -> bookService.delete();
            case 4 -> bookService.findByTitle();
            case 5 -> bookService.findAvailableBooks();
            case 6 -> bookService.findBookOfAAuthor();
            case 9 -> System.out.println("Coming back to the principal menu...");
            default -> throw new IllegalArgumentException("Invalid option you must enter the available options :)");
        }
    }
    public void userMenu(){
        System.out.println("1 - Insert new user");
        System.out.println("2 - Update a user");
        System.out.println("3 - Delete a user");
        System.out.println("4 - Find user by name");
        System.out.println("9 - >> Return to the main menu");

        System.out.print("RESP: ");
        int resp = Integer.parseInt(SCANNER.nextLine());
        switch (resp){
            case 1 -> userService.insert();
            case 2 -> userService.update();
            case 3 -> userService.delete();
            case 4 -> userService.findByName();
            case 9 -> System.out.println("Coming back to the principal menu...");
            default -> throw new IllegalArgumentException("Invalid option you must enter the available options :)");
        }
    }
    public void authorMenu(){
        System.out.println("1 - Insert new author");
        System.out.println("2 - Update a author");
        System.out.println("3 - Delete a author");
        System.out.println("4 - Find author by name");
        System.out.println("5 - Find author by their book");
        System.out.println("9 - >> Return to the main menu");

        System.out.print("RESP: ");
        int resp = Integer.parseInt(SCANNER.nextLine());
        switch (resp){
            case 1 -> authorService.insert();
            case 2 -> authorService.update();
            case 3 -> authorService.delete();
            case 4 -> authorService.findByName();
            case 5 -> authorService.findAuthorByWroteBook();
            case 9 -> System.out.println("Coming back to the principal menu...");
            default -> throw new IllegalArgumentException("Invalid option you must enter the available options :)");
        }
    }

    public void loanMenu(){
        System.out.println("1 - Register a new loan");
        System.out.println("2 - Return a book");
        System.out.println("3 - Find the book in the loan service by its status or by the loan status");
        System.out.println("4 - Find the user(s) with more than one loan");
        System.out.println("5 - Find all loans");
        System.out.println("9 - >> Return to the main menu");

        System.out.print("RESP: ");
        int resp = Integer.parseInt(SCANNER.nextLine());
        switch (resp){
            case 1 -> libraryLoanService.registerLoan();
            case 2 -> libraryLoanService.returnBook();
            case 3 -> libraryLoanService.findBooksBorrowedByStatus();
            case 4 -> libraryLoanService.findUsersWithMoreThanOneBookBorrowed();
            case 5 -> libraryLoanService.findAllLoan();
            case 9 -> System.out.println("Coming back to the principal menu...");
            default -> throw new IllegalArgumentException("Invalid option you must enter the available options :)");
        }
    }
    public void reportMenu(){
        System.out.println("1 - Book more borrowed");
        System.out.println("2 - Users with more loan");
        System.out.println("3 - Books borrowed in the moment");
        System.out.println("4 - Generate a payment fee for late loans");
        System.out.println("9 - >> Return to the main menu");

        System.out.print("RESP: ");
        int resp = Integer.parseInt(SCANNER.nextLine());

        switch (resp){
            case 1 -> bookService.bookMoreBorrowed();
            case 2 -> userService.findUserWithTheMostLoans();
            case 3 -> libraryLoanService.bookBorrowedInTheMoment();
            case 4 -> libraryLoanService.penaltyFeeForLateLoans();
            case 9 -> System.out.println("Coming back to the principal menu...");
            default -> throw new IllegalArgumentException("Invalid option you must enter the available options :)");
        }
    }
}
