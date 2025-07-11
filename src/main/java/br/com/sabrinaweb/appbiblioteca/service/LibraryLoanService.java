package br.com.sabrinaweb.appbiblioteca.service;

import br.com.sabrinaweb.appbiblioteca.model.dao.DaoFactory;
import br.com.sabrinaweb.appbiblioteca.model.dao.LibraryLoanDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.entities.LibraryLoan;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

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
                throw new BookNotAvailableForLoanException("Couldn't register the new loan, the book is already borrowed in the moment");
            } else if (libraryLoanDao.isLoanBookAvailable(idBook) != 0 && libraryLoanDao.isLoanBookAvailable(idBook) != 1) {
                throw new RuntimeException("Error while verifying if the book is available");
            }

            System.out.println("Enter the user id to insert: ");
            Integer idUser = Integer.parseInt(SCANNER.nextLine());

            User userToInsert = User.builder().id(idUser).build();
            Book bookToInsert = Book.builder().id(idBook).build();

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
        }
    }

    public void returnBook() {
        try {
            libraryLoanDao.findAllLoan().forEach(l -> System.out.printf("ID: %d, id_book: %d id_user: %d %n", l.getId(), l.getBook().getId(), l.getUser().getId()));
            System.out.println("Enter the loan id you want to return");
            Integer id = Integer.parseInt(SCANNER.nextLine());
            LibraryLoan loanFoundById = libraryLoanDao.findById(id).orElseThrow(() -> new InvalidIdException("The id is invalid"));
            if (loanFoundById.getStatus().equalsIgnoreCase("devolvido")){
                throw new RuntimeException("The book is already returned");
            }
            LibraryLoan libraryLoanToReturn = LibraryLoan.builder()
                    .id(loanFoundById.getId())
                    .status("devolvido")
                    .returnDate(LocalDate.now())
                    .build();
            libraryLoanDao.update(libraryLoanToReturn);
            log.info("The return happen with success!");
        }catch (RuntimeException e){
            log.error(e.getMessage());
        }
    }
    public void findBooksBorrowedByStatus() {
        System.out.println("Enter the status of the book that you want to find");
        String status  = SCANNER.nextLine();
        Map<Integer, Book> booksBorrowedByStatus = libraryLoanDao.findBooksBorrowedByStatus(status);

        booksBorrowedByStatus.forEach((id, book) -> System.out.printf("ID[%d] %s - %s - %s - %s %n", id, book.getTitle(), book.getGenre(), book.getPublisher(), book.getIsbn()));
    }
    public void findUsersWithMoreThanOneBookBorrowed() {
        log.info("Users found: ");
        libraryLoanDao.findUsersWithMoreThanOneBookBorrowed().forEach((id, user) -> System.out.printf("ID[%d] %s - %s - %s - %s %n", id, user.getName(), user.getEmail(), user.getPhone(), user.getAddress()));
    }
}
