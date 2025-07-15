package br.com.sabrinaweb.appbiblioteca.service;

import br.com.sabrinaweb.appbiblioteca.model.dao.DaoFactory;
import br.com.sabrinaweb.appbiblioteca.model.dao.LibraryLoanDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.entities.LibraryLoan;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.BookNotAvailableForLoanException;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.InvalidIdException;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.InvalidLoanException;
import lombok.extern.log4j.Log4j2;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Log4j2
public class LibraryLoanService {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final LibraryLoanDao libraryLoanDao = DaoFactory.createLoanDao();

    public void registerLoan() {
        try {
            System.out.println("Enter the id of the book you want to insert");
            Integer idBook = Integer.parseInt(SCANNER.nextLine());
            if (libraryLoanDao.isLoanBookAvailable(idBook) == 1) {
                throw new BookNotAvailableForLoanException("Couldn't register the new loan, the book is already borrowed in the moment or it's not available");
            } else if (libraryLoanDao.isLoanBookAvailable(idBook) != 0 && libraryLoanDao.isLoanBookAvailable(idBook) != 1) {
                throw new IllegalStateException("Error while verifying if the book is available, its not possible to verify if the book was available");
            }

            System.out.println("Enter the user id to insert: ");
            Integer idUser = Integer.parseInt(SCANNER.nextLine());

            User userToInsert = User.builder().id(idUser).build();
            Book bookToInsert = Book.builder().id(idBook).status("indisponível").build();

            LibraryLoan libraryLoan = LibraryLoan.builder()
                    .user(userToInsert)
                    .book(bookToInsert)
                    .status("emprestado")
                    .loanDate(LocalDate.now())
                    .dueDate(LocalDate.now().plusDays(15))
                    .build();
            libraryLoanDao.insert(libraryLoan);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }catch (SQLException ex){
            log.error("Error with the connection");
        }
    }
    public void findAllLoan() {
        libraryLoanDao.findAllLoan().forEach(l -> System.out.println( "ID ["+ l.getId() + "] - book id:" + l.getBook().getId() + " - user id:" + l.getUser().getId() + " - " + l.getStatus() + " - loan date: " + l.getLoanDate() + " - due date: " + l.getDueDate() + " - return date: " + l.getReturnDate()));
    }
    public void returnBook() {
        try {
            libraryLoanDao.findAllLoan().forEach(l -> System.out.printf("ID: %d, id_book: %d id_user: %d %s %n", l.getId(), l.getBook().getId(), l.getUser().getId(), l.getStatus()));
            System.out.println("Enter the loan id you want to return");
            Integer id = Integer.parseInt(SCANNER.nextLine());
            LibraryLoan loanFoundById = libraryLoanDao.findById(id).orElseThrow(() -> new InvalidIdException("The id is invalid"));

            if (loanFoundById.getStatus().equalsIgnoreCase("devolvido")) {
                throw new InvalidLoanException("The book is already returned");
            }
            log.info("ID:[{}] - book_ID:{} - user_id:{} - {} - {} - {}", loanFoundById.getId(), loanFoundById.getBook().getId(), loanFoundById.getUser().getId(), loanFoundById.getStatus(), loanFoundById.getDueDate(), loanFoundById.getLoanDate());
            LibraryLoan libraryLoanToReturn = LibraryLoan.builder()
                    .id(loanFoundById.getId())
                    .book(loanFoundById.getBook())
                    .user(loanFoundById.getUser())
                    .status("devolvido")
                    .returnDate(LocalDate.now())
                    .build();

            libraryLoanToReturn.getBook().setStatus("disponível");

            libraryLoanDao.update(libraryLoanToReturn);
        } catch (NumberFormatException | InvalidLoanException e) {
            log.error(e.getMessage());
        }catch (SQLException ex){
            log.info("Error with the connection");
        }
    }

    public void findBooksBorrowedByStatus() {
        System.out.println("Enter the status of the book or the loan that you want to find");
        String status = SCANNER.nextLine();
        Map<Integer, Book> booksBorrowedByStatus = libraryLoanDao.findBooksBorrowedByStatus(status);
        booksBorrowedByStatus.forEach((id, book) -> System.out.printf("ID_Loan[%d] Book - %d - %s - %s - %s - %s - %s %n", id, book.getId(), book.getTitle(), book.getGenre(), book.getPublisher(), book.getIsbn(), book.getStatus()));
    }

    public void findUsersWithMoreThanOneBookBorrowed() {
        log.info("User(s) with more than one book borrowed found: ");
        libraryLoanDao.findUsersWithMoreThanOneBookBorrowed().forEach((ids, user) -> System.out.println("ID of loans: " +ids + ": user: ID["+ user.getId() + "], " + user.getName() + ", " + user.getPhone() + ", " + user.getEmail() + ", " + user.getAddress()));
    }

    public void bookMoreBorrowed(){
        log.info("Book more borrowed: ");

        
        libraryLoanDao.bookMoreBorrowed().ifPresent(System.out::println);
    }
}
