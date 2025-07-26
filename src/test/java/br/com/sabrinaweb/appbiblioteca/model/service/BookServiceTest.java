package br.com.sabrinaweb.appbiblioteca.model.service;

import br.com.sabrinaweb.appbiblioteca.model.dao.BookDao;
import br.com.sabrinaweb.appbiblioteca.model.dao.BookDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.InvalidIdException;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    private BookDao bookDao;
    private BookService bookService;

    @BeforeEach
    void setUp(){
        bookDao = new BookDao() {
            @Override
            public void insert(Book book) {
                System.out.println("Simulated insertion to test the book " + book.getTitle());

            }

            @Override
            public void update(Book book) {
                System.out.println("Simulated update to test the book " + book.getTitle());

            }

            @Override
            public void deleteById(Integer id) {
                System.out.println("Simulated exclusion to test the book ");

            }

            @Override
            public List<Book> findAllBooks() {
                System.out.println("Simulated findAllBooks to test the book " );
                return List.of();
            }

            @Override
            public List<Book> findByTitle(String title) {
                System.out.println("Simulated findByTittle to test the book " + title);
                if (title.equalsIgnoreCase("Harry Potter"))
                    return List.of(Book.builder()
                            .title(title)
                            .publisher("")
                            .genre("Aventura")
                            .isbn("978-85-0000000-1")
                            .year_public(2001)
                            .numberPages(275)
                            .build());
                return List.of();
            }

            @Override
            public List<Book> findAvailableBooks() {
                System.out.println("Simulated findAvailableBooks to test the book ");

                return List.of();
            }

            @Override
            public List<Book> findAllBooksOfAAuthor(Integer id_author) {
                System.out.println("Simulated findAllBooksOfAAAuthor to test the book ");
                return List.of();
            }

            @Override
            public Optional<Book> findById(Integer id) {
                System.out.println("Simulated findById to test the book");
                if (id == 1){
                    return Optional.of(Book.builder().id(1).title("").year_public(0).publisher("").isbn("").status("").genre("").numberPages(0).build());
                }
                return Optional.empty();
            }

            @Override
            public Optional<Book> bookMoreBorrowed() {
                return Optional.empty();
            }
        };
    }
    @Test
    @DisplayName("If the fields are empty, an IllegalArgumentException should be thrown")
    void insert_ThrowIllegalArgumentException_WhenFieldsAreEmpty() {
        String scannerTest = "\n\n\n\n\n\n\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        bookService = new BookService(sc, bookDao);
        Assertions.assertThrows(IllegalArgumentException.class, () -> bookService.insert());
    }
    
    @Test
    @DisplayName("If the fields are all fill in, an IllegalArgumentException shouldn't be thrown")
    void insert_DoNotThrowIllegalArgumentException_WhenFieldsAreAllFillIn() {
        String scannerTest = "TittleExample\n978-85-0000000-x\nPublisherExample\nGenreExample\n2025\n205";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        bookService = new BookService(sc, bookDao);
        Assertions.assertDoesNotThrow(() -> bookService.insert());
    }

    @Test
    @DisplayName("If the id is null, a NumberFormatException should be thrown")
    void delete_ThrowNumberFormatException_WhenTheFieldIsEmpty() {
        String scannerTest = "\ny";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        bookService = new BookService(sc, bookDao);
        Assertions.assertThrows(NumberFormatException.class, () -> bookService.delete());
    }

    @Test
    @DisplayName("If the id is valid, a NumberFormatException shouldn't be thrown")
    void delete_DoNotThrowNumberFormatException_WhenValidId() {
        String scannerTest = "1\ny";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        bookService = new BookService(sc, bookDao);
        Assertions.assertDoesNotThrow(() -> bookService.delete());
    }

    @Test
    @DisplayName("If the id is null, a NumberFormatException should be thrown")
    void update_ThrowNumberFormatException_WhenTheFieldIsEmpty() {
        String scannerTest = "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        bookService = new BookService(sc, bookDao);
        Assertions.assertThrows(IllegalArgumentException.class, () -> bookService.update());
    }
    @Test
    @DisplayName("If the id is valid, a NumberFormatException shouldn't be thrown")
    void update_DoNotThrowNumberFormatException_WhenValidId() {
        String scannerTest = "1\n\n\n0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        bookService = new BookService(sc, bookDao);
        Assertions.assertDoesNotThrow(() -> bookService.update());
    }
    @Test
    @DisplayName("If the book was not found, an IllegalArgumentException should be thrown")
    void update_ThrowIllegalArgumentException_WhenUserNotFound() {
        String scannerTest = "1000";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        bookService = new BookService(sc, bookDao);
        Assertions.assertThrows(IllegalArgumentException.class, () -> bookService.update());
    }

    @Test
    @DisplayName("If the id is null, a NumberFormatException should be thrown")
    void findBookOfAAuthor_ThrowNumberFormatException_WhenTheFieldIsEmpty() {
        String scannerTest = "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        bookService = new BookService(sc, bookDao);
        Assertions.assertThrows(NumberFormatException.class, () -> bookService.findBookOfAAuthor());
    }
    @Test
    @DisplayName("If the id is valid, a NumberFormatException shouldn't be thrown")
    void findBookOfAAuthor_DoNotThrowNumberFormatException_WhenTheFieldIsEmpty() {
        String scannerTest = "1";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        bookService = new BookService(sc, bookDao);
        Assertions.assertDoesNotThrow(() -> bookService.findBookOfAAuthor());
    }

    @Test
    @DisplayName("If the id is lower or equal zero, an InvalidIdException should be thrown")
    void invalidId_ThrowInvalidIdException_WhenIdIsInvalid() {
        bookService = new BookService();
        Assertions.assertThrows(InvalidIdException.class, () -> bookService.invalidId(0));
        Assertions.assertThrows(InvalidIdException.class, () -> bookService.invalidId(-1));
    }
    @Test
    @DisplayName("If the title field was empty or do not exist a book with that title, it will return an empty list")
    void findByName_ReturnEmptyList_WhenFieldIsEmpty(){
        String scannerTestEmpty = "\n";
        String scannerTestInvalidName = "O Hobbit";

        List<Book> books = new ArrayList<>();

        Assertions.assertEquals(books, bookDao.findByTitle(scannerTestEmpty));
        Assertions.assertEquals(books, bookDao.findByTitle(scannerTestInvalidName));
    }
    @Test
    @DisplayName("If the title field was not empty and the title is valid, it will return a book")
    void findByName_ReturnAFoundBook_WhenTitleIsValid(){
        String scannerTest = "Harry Potter";
        List<Book> books = new ArrayList<>();
        books.add(Book.builder()
                .title(scannerTest)
                .publisher("")
                .genre("Aventura")
                .isbn("978-85-0000000-1")
                .year_public(2001)
                .numberPages(275)
                .build());

        Assertions.assertEquals(books, bookDao.findByTitle(scannerTest));
    }
}