package br.com.sabrinaweb.appbiblioteca.model.service;

import br.com.sabrinaweb.appbiblioteca.model.dao.AuthorDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Author;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.InvalidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class AuthorServiceTest {
    private AuthorDao authorDao;
    private AuthorService authorService;

    @BeforeEach
    public void setUp(){
        authorDao = new AuthorDao() {
            @Override
            public void insert(Author author) {
                System.out.println("Simulated insertion to test the author "+ author.getName());
            }
            @Override
            public void deleteById(Integer id) {
                System.out.println("Simulated exclusion to test the author");
            }
            @Override
            public void update(Author author) {
                System.out.println("Simulated update to test the author" + author.getName());
            }
            @Override
            public List<Author> findAllAuthors() {
                System.out.println("Simulated findAllAuthors to test the author");
                return List.of();
            }
            @Override
            public List<Author> findByName(String name) {
                System.out.println("Simulated findByName to test the author");
                return List.of();
            }
            @Override
            public Optional<Author> findById(Integer id) {
                System.out.println("Simulated findById to test the author");
                if (id == 1){
                    return Optional.of(Author.builder().id(1).name("").birthdate(LocalDate.now()).nationality("").build());
                }
                return Optional.empty();
            }
            @Override
            public List<Author> findAuthorByWroteBook(Integer idBook) {
                System.out.println("Simulated findByWroteBook to test the author");
                return List.of();
            }
        };
    }
    @Test
    @DisplayName("If the fields are empty, an IllegalArgumentException will be thrown")
    void insert_ThrowIllegalArgumentException_WhenFieldsAreEmpty() {
        String scannerTest = "\n\n\n\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        authorService = new AuthorService(sc, authorDao);
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorService.insert());
    }

    @Test
    @DisplayName("If the fields are not empty, an IllegalArgumentException won't be thrown")
    void insert_DoesNotThrowIllegalArgumentException_WhenFieldsAreAllFillIn() {
        String scannerTest = "Maria Curry\nBritânica\n1960-03-25";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        authorService = new AuthorService(sc, authorDao);
        Assertions.assertDoesNotThrow(() -> authorService.insert());
    }

    @Test
    @DisplayName("If the id is null a NumberFormatException should be thrown")
    void deleteById_ThrowNumberFormatException_WhenFieldIsEmpty(){
        String scannerTest = "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        authorService = new AuthorService(sc, authorDao);
        Assertions.assertThrows(NumberFormatException.class, () -> authorService.delete());
    }

    @Test
    @DisplayName("If the id is lower or equal than zero, an InvalidIdException should be thrown")
    void invalidId_ThrowInvalidIdException_WhenIdIsLowerOrEqualThanZero(){
        authorService = new AuthorService();
        Assertions.assertThrows(InvalidIdException.class, () -> authorService.invalidId(0));
        Assertions.assertThrows(InvalidIdException.class, () -> authorService.invalidId(-1));
    }

    @Test
    @DisplayName("It shouldn't throw any NumberFormatException when the id is valid")
    void deleteById_DoesNotThrowAnyNumberFormatException_WhenValidId(){
        String scannerTest = "1\ny";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        authorService = new AuthorService(sc, authorDao);
       Assertions.assertDoesNotThrow(() -> authorService.delete());
    }

    @Test
    @DisplayName("If the id is null, a NumberFormatException should be thrown")
    void update_ThrowNumberFormatException_WhenFieldIsEmpty(){
        String scannerTest = "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        authorService = new AuthorService(sc, authorDao);
        Assertions.assertThrows(NumberFormatException.class, () -> authorService.update());
    }
    @Test
    @DisplayName("If the author was not found, an IllegalArgumentException should be thrown")
    void update_ThrowIllegalArgumentException_WhenAuthorWasNotFound(){
        String scannerTest = "1000";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        authorService = new AuthorService(sc, authorDao);
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorService.update());
    }
    @Test
    @DisplayName("If the id is valid, a NumberFormatException shouldn't be thrown")
    void update_DoNotThrowNumberFormatException_WhenValidId() {
        String scannerTest = "1\n\n\n\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        authorService = new AuthorService(sc, authorDao);
        Assertions.assertDoesNotThrow(() -> authorService.update());
    }
    @Test
    @DisplayName("If the id is null a NumberFormatException should be thrown")
    void findAuthorByWroteBook_ThrowNumberFormatException_WhenFieldIsEmpty(){
        String scannerTest = "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        authorService = new AuthorService(sc, authorDao);
        Assertions.assertThrows(NumberFormatException.class, () -> authorService.findAuthorByWroteBook());
    }

    @Test
    @DisplayName("It shouldn't throw any NumberFormatException when the id is valid")
    void findAuthorByWroteBook_DoesNotThrowAnyNumberFormatException_WhenValidId(){
        String scannerTest = "1";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        authorService = new AuthorService(sc, authorDao);
        Assertions.assertDoesNotThrow(() -> authorService.findAuthorByWroteBook());
    }
}