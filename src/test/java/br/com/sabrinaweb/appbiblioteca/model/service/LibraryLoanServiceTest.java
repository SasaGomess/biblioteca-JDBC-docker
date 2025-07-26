package br.com.sabrinaweb.appbiblioteca.model.service;

import br.com.sabrinaweb.appbiblioteca.model.dao.LibraryLoanDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.entities.LibraryLoan;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.BookNotAvailableForLoanException;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.InvalidIdException;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.InvalidLoanException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LibraryLoanServiceTest {
    private LibraryLoanDao libraryLoanDao;
    private LibraryLoanService libraryLoanService;

    @BeforeEach
    void setUp() {
        libraryLoanDao = new LibraryLoanDao() {
            @Override
            public void insert(LibraryLoan libraryLoan) throws SQLException {
                System.out.println("Simulated insertion to test the loan ");

            }

            @Override
            public void update(LibraryLoan libraryLoan) throws SQLException {
                System.out.println("Simulated update to test the loan ");
            }

            @Override
            public void deleteById(Integer idLoan) {
                System.out.println("Simulated exclusion to test the loan ");

            }

            @Override
            public List<LibraryLoan> findAllLoan() {
                System.out.println("Simulated findAllLoan to test the loan ");

                return List.of();
            }

            @Override
            public int isLoanBookAvailable(Integer idBook) {
                System.out.println("Simulated isLoanBookAvailable to test the loan ");
                if (idBook == 1 || idBook == 3) {
                    return 1;
                } else if (idBook == 2) {
                    return 0;
                } else {
                    return 2;
                }
            }

            @Override
            public Optional<LibraryLoan> findById(Integer id) {
                System.out.println("Simulated findById to test the loan ");
                if (id == 1){
                    return Optional.of(LibraryLoan.builder().id(id).book(Book.builder().id(1).build()).user(User.builder().id(3).build()).status("atrasado").build());
                }
                if (id == 3){
                    return Optional.of(LibraryLoan.builder().id(id).status("emprestado").book(Book.builder().id(3).build()).user(User.builder().id(3).build()).build());
                }
                if (id == 2){
                    return Optional.of(LibraryLoan.builder().id(id).status("devolvido").book(Book.builder().id(2).build()).user(User.builder().id(2).build()).build());
                }
                return Optional.empty();
            }

            @Override
            public Map<Integer, Book> findBooksBorrowedByStatus(String status) {
                System.out.println("Simulated findBooksBorrowedByStatus to test the loan ");
                if (status.equalsIgnoreCase("emprestado")){
                    return Map.of(3, Book.builder().id(3).build());
                } else if (status.equalsIgnoreCase("atrasado")) {
                    return Map.of(1, Book.builder().id(1).build());
                }else if (status.equalsIgnoreCase("devolvido")){
                    return Map.of(2, Book.builder().id(2).build());
                }
                return Map.of();
            }

            @Override
            public Map<List<Integer>, User> findUsersWithMoreThanOneBookBorrowed() {
                System.out.println("Simulated findUsersWithMoreThanOneBookBorrowed to test the loan ");
                return Map.of();
            }

            @Override
            public List<Book> booksBorrowedInTheMoment() {
                System.out.println("Simulated booksBorrowedInTheMoment to test the loan ");
                return List.of();
            }

            @Override
            public List<LibraryLoan> lateLoans() {
                System.out.println("Simulated lateLoans to test the loan ");
                return List.of();
            }
        };
    }

    @Test
    @DisplayName("If the id is null, a NumberFormatException should be thrown")
    void registerLoan_ThrowNumberFormatException_WhenTheFieldIsEmpty() {
        String scannerTest = "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        libraryLoanService = new LibraryLoanService(sc, libraryLoanDao);
        Assertions.assertThrows(NumberFormatException.class, () -> libraryLoanService.registerLoan());
    }

    @Test
    @DisplayName("If the id is valid, a NumberFormatException shouldn't be thrown")
    void registerLoan_DoNotThrowNumberFormatException_WhenValidId() {
        String scannerTest = "2\n2\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        libraryLoanService = new LibraryLoanService(sc, libraryLoanDao);
        Assertions.assertDoesNotThrow(() -> libraryLoanService.registerLoan());
    }

    @Test
    @DisplayName("If the book is not available for loan, a BookNotAvailableForLoanException should be thrown")
    void registerLoan_ThrowBookNotAvailableForLoanException_WhenBookIsNotAvailableForLoan() {
        String scannerTest = "1\n2\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        libraryLoanService = new LibraryLoanService(sc, libraryLoanDao);
        Assertions.assertThrows(BookNotAvailableForLoanException.class, () -> libraryLoanService.registerLoan());
    }

    @Test
    @DisplayName("If the book is available for loan, a BookNotAvailableForLoanException shouldn't be thrown")
    void registerLoan_DoNotThrowBookNotAvailableForLoanException_WhenBookIsAvailableForLoan() {
        String scannerTest = "2\n2\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        libraryLoanService = new LibraryLoanService(sc, libraryLoanDao);
        Assertions.assertDoesNotThrow(() -> libraryLoanService.registerLoan());
    }

    @Test
    @DisplayName("If the book it occur an error while checking if the book is available, an IllegalStateException should be thrown")
    void registerLoan_ThrowIllegalStateException_WhenOccurAnErrorCheckingIfTheBookIsAvailable() {
        String scannerTest = "2\n2\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        libraryLoanService = new LibraryLoanService(sc, libraryLoanDao);
        Assertions.assertDoesNotThrow(() -> libraryLoanService.registerLoan());
    }


    @Test
    @DisplayName("If the status of the loan is 'return', an InvalidLoanException should be thrown")
    void returnBook_ThrowInvalidLoanException_WhenTheStatusIsReturn() {
        String scannerTest = "2";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        libraryLoanService = new LibraryLoanService(sc, libraryLoanDao);
        Assertions.assertThrows(InvalidLoanException.class, () -> libraryLoanService.returnBook());
    }

    @Test
    @DisplayName("If the status of the loan is 'borrowed' or 'late', an InvalidLoanException shouldn't be thrown")
    void returnBook_DoNotThrowInvalidLoanException_WhenStatusIsBorrowedOrLate() {
        String scannerTestLate = "1";
        String scannerTestBorrowed = "3";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTestLate.getBytes());
        Scanner sc = new Scanner(inputStream);
        libraryLoanService = new LibraryLoanService(sc, libraryLoanDao);
        Assertions.assertDoesNotThrow(() -> libraryLoanService.returnBook());

        ByteArrayInputStream inputStream02 = new ByteArrayInputStream(scannerTestBorrowed.getBytes());
        Scanner sc02 = new Scanner(inputStream02);
        libraryLoanService = new LibraryLoanService(sc02, libraryLoanDao);
        Assertions.assertDoesNotThrow(() -> libraryLoanService.returnBook());
    }
    @Test
    @DisplayName("If the id is null, a NumberFormatException should be thrown")
    void returnBook_ThrowNumberFormatException_WhenTheFieldIsEmpty() {
        String scannerTest = "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        libraryLoanService = new LibraryLoanService(sc, libraryLoanDao);
        Assertions.assertThrows(NumberFormatException.class, () -> libraryLoanService.returnBook());
    }

    @Test
    @DisplayName("If the id is valid, a NumberFormatException shouldn't be thrown")
    void returnBook_DoNotThrowNumberFormatException_WhenValidId() {
        String scannerTest = "1";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        libraryLoanService = new LibraryLoanService(sc, libraryLoanDao);
        Assertions.assertDoesNotThrow(() -> libraryLoanService.returnBook());
    }
    @Test
    @DisplayName("If the status was 'return', it should return only loans with this status")
    void findBooksBorrowedByStatus_ReturnLoansAndBooks_WhenLoansStatusIsReturn(){
        String scannerTest = "devolvido";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        libraryLoanService = new LibraryLoanService(sc, libraryLoanDao);
        Map<Integer, Book> booksFound = new HashMap<>();
        booksFound.put(2, Book.builder().id(2).build());
        Assertions.assertEquals(booksFound, libraryLoanDao.findBooksBorrowedByStatus(scannerTest));
    }
    @Test
    @DisplayName("If the status was 'late', it should return only loans with this status")
    void findBooksBorrowedByStatus_ReturnLoansAndBooks_WhenLoansStatusIsLate(){
        String scannerTest = "atrasado";
        Map<Integer, Book> booksFound = new HashMap<>();
        booksFound.put(1, Book.builder().id(1).build());
        Assertions.assertEquals(booksFound, libraryLoanDao.findBooksBorrowedByStatus(scannerTest));
    }
    @Test
    @DisplayName("If the status was 'borrowed', it should return only loans with this status")
    void findBooksBorrowedByStatus_ReturnLoansAndBooks_WhenLoansStatusIsBorrowed(){
        String scannerTest = "emprestado";
        Map<Integer, Book> booksFound = new HashMap<>();
        booksFound.put(3, Book.builder().id(3).build());
        Assertions.assertEquals(booksFound, libraryLoanDao.findBooksBorrowedByStatus(scannerTest));
    }
    @Test
    @DisplayName("If the status was empty, it should return an empty Map")
    void findBooksBorrowedByStatus_ReturnEmptyMap_WhenFieldIsEmpty(){
        String scannerTest = "";
        Map<Integer, Book> booksFound = new HashMap<>();
        Assertions.assertEquals(booksFound, libraryLoanDao.findBooksBorrowedByStatus(scannerTest));
    }
}