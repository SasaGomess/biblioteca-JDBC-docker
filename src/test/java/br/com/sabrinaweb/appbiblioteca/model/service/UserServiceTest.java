package br.com.sabrinaweb.appbiblioteca.model.service;

import br.com.sabrinaweb.appbiblioteca.model.dao.UserDao;
import br.com.sabrinaweb.appbiblioteca.model.dao.impl.UserDaoJdbc;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;
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

class UserServiceTest {
    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp(){
        userDao = new UserDao() {
            @Override
            public void insert(User user) {
                System.out.println("Simulated insertion to test the user " + user.getName());
            }

            @Override
            public void update(User user) {
                System.out.println("Simulated update to test the user " + user.getName());
            }

            @Override
            public void deleteById(Integer id) {
                System.out.println("Simulated exclusion to test the user" );
            }

            @Override
            public List<User> findAllUsers() {
                System.out.println("Simulated find All Users to test the user");
                return List.of();
            }

            @Override
            public List<User> findByName(String name) {
                System.out.println("Simulated findByName to test the user: " + name);
                if (name.equalsIgnoreCase("Maria Silva"))
                    return List.of(User.builder()
                        .name("Maria Silva")
                        .email("maria@gmail.com")
                        .phone("123321232")
                        .address("Rua das Flores, São Paulo, São Paulo")
                        .build());
                return List.of();
            }

            @Override
            public Optional<User> findById(Integer id) {
                System.out.println("Simulated findById to test the user");
                if (id == 1){
                    return Optional.of(User.builder().id(1).name("").email("").address("").phone("").build());
                }
                return Optional.empty();
            }

            @Override
            public Optional<User> userWithMostLoans() {
                System.out.println("Simulated users with the most loans to test the user");
                return Optional.empty();
            }
        };
    }

    @Test
    @DisplayName("If the fields are empty, an IllegalArgumentException should be thrown")
    void insert_ThrowIllegalArgumentException_WhenFieldsAreEmpty() {
        String scannerTest = "\n\n\n\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        userService = new UserService(sc, userDao);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.insert());
    }

    @Test
    @DisplayName("If the fields are all fill in, an IllegalArgumentException shouldn't be thrown")
    void insert_DoNotThrowIllegalArgumentException_WhenFieldsAreAllFillIn() {
        String scannerTest = "NameUserExample\nemailnoReply.@gmail.com\n1199999999\nStreet, City, State";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        userService = new UserService(sc, userDao);
        Assertions.assertDoesNotThrow(() -> userService.insert());
    }

    @Test
    @DisplayName("If the id is null, a NumberFormatException should be thrown")
    void delete_ThrowNumberFormatException_WhenTheFieldIsEmpty() {
        String scannerTest = "\ny";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        userService = new UserService(sc, userDao);
        Assertions.assertThrows(NumberFormatException.class, () -> userService.delete());
    }
    @Test
    @DisplayName("If the id is valid, a NumberFormatException shouldn't be thrown")
    void delete_DoNotThrowNumberFormatException_WhenValidId() {
        String scannerTest = "1\ny";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        userService = new UserService(sc, userDao);
        Assertions.assertDoesNotThrow(() -> userService.delete());
    }

    @Test
    @DisplayName("If the id is null, a NumberFormatException should be thrown")
    void update_ThrowNumberFormatException_WhenTheFieldIsEmpty() {
        String scannerTest = "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        userService = new UserService(sc, userDao);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.update());
    }
    @Test
    @DisplayName("If the id is valid, a NumberFormatException shouldn't be thrown")
    void update_DoNotThrowNumberFormatException_WhenValidId() {
        String scannerTest = "1\n\n\n\n\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        userService = new UserService(sc, userDao);
        Assertions.assertDoesNotThrow(() -> userService.update());
    }
    @Test
    @DisplayName("If the user was not found, a UserNotFoundException should be thrown")
    void update_ThrowUserNotFoundException_WhenUserNotFound() {
        String scannerTest = "1000";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(scannerTest.getBytes());
        Scanner sc = new Scanner(inputStream);
        userService = new UserService(sc, userDao);
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.update());
    }
    @Test
    @DisplayName("If the id is lower or equal zero, an InvalidIdException should be thrown")
    void invalidId_ThrowInvalidIdException_WhenIdIsInvalid() {
        userService = new UserService();
        Assertions.assertThrows(InvalidIdException.class, () -> userService.invalidId(0));
        Assertions.assertThrows(InvalidIdException.class, () -> userService.invalidId(-1));
    }
    @Test
    @DisplayName("If the name field was empty or do not exist a user with that name, it will return an empty list")
    void findByName_ReturnEmptyList_WhenFieldIsEmpty(){
        String scannerTestEmpty = "\n";
        String scannerTestInvalidName = "Julia Faria";

        List<User> users = new ArrayList<>();

        Assertions.assertEquals(users, userDao.findByName(scannerTestEmpty));
        Assertions.assertEquals(users, userDao.findByName(scannerTestInvalidName));
    }
    @Test
    @DisplayName("If the name field was not empty and the name is valid, it will return a user")
    void findByName_ReturnAFoundUser_WhenNameIsValid(){
        String scannerTest = "Maria Silva";
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .name("Maria Silva")
                .email("maria@gmail.com")
                .phone("123321232")
                .address("Rua das Flores, São Paulo, São Paulo")
                .build());

        Assertions.assertEquals(users, userDao.findByName(scannerTest));

    }
}