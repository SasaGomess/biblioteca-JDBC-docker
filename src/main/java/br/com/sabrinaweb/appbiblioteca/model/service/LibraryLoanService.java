package br.com.sabrinaweb.appbiblioteca.model.service;

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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Log4j2
public class LibraryLoanService {

    private Scanner scanner;
    private LibraryLoanDao libraryLoanDao;

    public LibraryLoanService(Scanner scanner, LibraryLoanDao libraryLoanDao) {
        this.scanner = scanner;
        this.libraryLoanDao = libraryLoanDao;
    }

    public LibraryLoanService() {
    }

    public void registerLoan() {
        try {
            System.out.println("Enter the id of the book you want to insert");
            Integer idBook = Integer.parseInt(scanner.nextLine());
            invalidId(idBook);
            if (libraryLoanDao.isLoanBookAvailable(idBook) == 1) {
                throw new BookNotAvailableForLoanException("Couldn't register the new loan, the book is already borrowed in the moment or it's not available");
            } else if (libraryLoanDao.isLoanBookAvailable(idBook) != 0 && libraryLoanDao.isLoanBookAvailable(idBook) != 1) {
                throw new IllegalStateException("Error while verifying if the book is available, its not possible to verify if the book was available");
            }

            System.out.println("Enter the user id to insert: ");
            Integer idUser = Integer.parseInt(scanner.nextLine());
            invalidId(idUser);
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
            log.info("The due date of the loan is: '{}' you should return the book until this date, otherwise the loan will be set as 'atrasado' and a penalty rate will be charged if it exceed 2 days", libraryLoan.getDueDate());
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
            Integer id = Integer.parseInt(scanner.nextLine());
            invalidId(id);
            LibraryLoan loanFoundById = libraryLoanDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Could not find the loan by the specified id"));

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

        }catch (SQLException ex){
            log.info("Error with the connection");
        }
    }

    public void findBooksBorrowedByStatus() {
        System.out.println("Enter the status of the book or the loan that you want to find");
        String status = scanner.nextLine();
        Map<Integer, Book> booksBorrowedByStatus = libraryLoanDao.findBooksBorrowedByStatus(status);
        booksBorrowedByStatus.forEach((id, book) -> System.out.printf("ID_Loan[%d] Book - %d - %s - %s - %s - %s - %s %n", id, book.getId(), book.getTitle(), book.getGenre(), book.getPublisher(), book.getIsbn(), book.getStatus()));
    }

    public void findUsersWithMoreThanOneBookBorrowed() {
        log.info("User(s) with more than one book borrowed found: ");
        libraryLoanDao.findUsersWithMoreThanOneBookBorrowed().forEach((ids, user) -> System.out.println("ID of loans: " +ids + ": user: ID["+ user.getId() + "], " + user.getName() + ", " + user.getPhone() + ", " + user.getEmail() + ", " + user.getAddress()));
    }

    public void bookBorrowedInTheMoment(){
        log.info("Books borrowed in the moment found: ");
        libraryLoanDao.booksBorrowedInTheMoment().forEach(b -> System.out.printf("ID:[%d] - %s, %s, %s, %s %d %n", b.getId(), b.getTitle(), b.getIsbn(), b.getGenre(), b.getPublisher(), b.getNumberPages()));
    }
    public void penaltyFeeForLateLoans(){
        List<LibraryLoan> libraryLoans = libraryLoanDao.lateLoans();

        if (!libraryLoans.isEmpty()){
            libraryLoans.forEach(p -> System.out.println("IDs of late loan(s) >> " + p.getId()));
            libraryLoans.stream().map(LibraryLoan::getDueDate)
                    .map(dueDate -> ChronoUnit.DAYS.between(dueDate, LocalDate.now()))
                    .mapToDouble(days -> days > 2L ? days * 0.5 : 0)
                    .forEach(totalFee -> System.out.printf("Total fee of the late loan to pay >> R$ %.2f %n", totalFee));
        }
    }
    public void invalidId(Integer id) {
        if (id <= 0) throw new InvalidIdException("The id is lower or equal 0, you should enter a valid id");
    }
}
