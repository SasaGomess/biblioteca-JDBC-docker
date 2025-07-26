package br.com.sabrinaweb.appbiblioteca.model.service;

import br.com.sabrinaweb.appbiblioteca.model.dao.DaoFactory;
import br.com.sabrinaweb.appbiblioteca.model.dao.UserDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.InvalidIdException;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.UserNotFoundException;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Scanner;

@Log4j2
public class UserService {
    private Scanner scanner;

    private UserDao userDao;

    public UserService(Scanner scanner, UserDao userDao) {
        this.scanner = scanner;
        this.userDao = userDao;
    }

    public UserService() {
    }

    public void insert() {

        System.out.println("Enter the user name to insert");
        String name = scanner.nextLine();
        System.out.println("Enter the user email: ");
        String email = scanner.nextLine();
        System.out.println("Enter the user phone: ");
        String phone = scanner.nextLine();
        System.out.println("Enter the user address: (STREET/CITY/STATE)");
        String address = scanner.nextLine();
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty())
            throw new IllegalArgumentException("You need to enter all fields to insert a valid user, otherwise you won't be able to insert the user");
        User user = User.builder().name(name).email(email).phone(phone).address(address).build();
        userDao.insert(user);
    }

    public void delete() {

        userDao.findAllUsers().forEach(u -> System.out.printf("[%d] - %s, %s, %s, %s %n", u.getId(), u.getName(), u.getEmail(), u.getAddress(), u.getPhone()));

        System.out.println("Enter the user id to delete");
        Integer id = Integer.parseInt(scanner.nextLine());
        invalidId(id);
        System.out.printf("Are you sure that you want to delete the user: %d [Y/N]%n", id);
        String resp = scanner.nextLine();
        if (resp.equalsIgnoreCase("y")) userDao.deleteById(id);
    }

    public void update() {

        System.out.println("Enter the user id to find");
        Integer id = Integer.parseInt(scanner.nextLine());
        invalidId(id);
        User userFoundById = userDao.findById(id).orElseThrow(() -> new UserNotFoundException("Could not find the user by the specified id"));

        log.info("ID:[{}] - {} - {} - {} - {}", userFoundById.getId(), userFoundById.getName(), userFoundById.getEmail(), userFoundById.getPhone(), userFoundById.getAddress());

        System.out.print("Enter the new user name or empty to keep the same: ");
        String name = scanner.nextLine();
        System.out.print("Enter the new user email or empty to keep the same: ");
        String email = scanner.nextLine();
        System.out.print("Enter the new user phone or empty to keep the same: ");
        String phone = scanner.nextLine();
        System.out.print("Enter the new user address or empty to keep the same: (STREET/CITY/STATE): ");
        String address = scanner.nextLine();

        name = name.isEmpty() ? userFoundById.getName() : name;
        email = email.isEmpty() ? userFoundById.getEmail() : email;
        phone = phone.isEmpty() ? userFoundById.getPhone() : phone;
        address = address.isEmpty() ? userFoundById.getAddress() : address;

        User userToUpdate = User.builder()
                .id(userFoundById.getId())
                .name(name)
                .email(email)
                .address(address)
                .phone(phone)
                .build();
        userDao.update(userToUpdate);
    }

    public void findByName() {
        System.out.println("Enter the user name to found");
        String name = scanner.nextLine();
        List<User> userByName = userDao.findByName(name);
        log.info("User found '{}'", userByName);
    }

    public void findUserWithTheMostLoans() {
        log.info("User found with the most loans: ");
        userDao.userWithMostLoans().ifPresent(u -> System.out.printf("ID:[%d] - %s, %s, %s, %s %n", u.getId(), u.getName(), u.getEmail(), u.getPhone(), u.getAddress()));
    }

    public void invalidId(Integer id) {
        if (id <= 0) throw new InvalidIdException("The id is lower or equal 0, you should enter a valid id");
    }
}
