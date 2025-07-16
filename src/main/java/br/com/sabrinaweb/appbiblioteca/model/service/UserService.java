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
    private static final Scanner SCANNER = new Scanner(System.in);

    private final UserDao userDao = DaoFactory.createUserDao();

    public void insert() {
        System.out.println("Enter the user name to insert");
        String name = SCANNER.nextLine();
        System.out.println("Enter the user email: ");
        String email = SCANNER.nextLine();
        System.out.println("Enter the user phone: ");
        String phone = SCANNER.nextLine();
        System.out.println("Enter the user address: (STREET/CITY/STATE)");
        String address = SCANNER.nextLine();
        User user = User.builder().name(name).email(email).phone(phone).address(address).build();
        userDao.insert(user);
    }

    public void delete() {
        try {
            userDao.findAllUsers().forEach(u -> System.out.printf("[%d] - %s, %s, %s, %s %n", u.getId(), u.getName(), u.getEmail(), u.getAddress(), u.getPhone()));

            System.out.println("Enter the user id to delete");
            Integer id = Integer.parseInt(SCANNER.nextLine());

            if (id < 0) throw new InvalidIdException("The id is null or equal 0, you should enter a valid id");

            System.out.printf("Are you sure that you want to delete the user: %d [Y/N]%n", id);
            String resp = SCANNER.nextLine();
            if (resp.equalsIgnoreCase("y")) userDao.deleteById(id);
        } catch (NumberFormatException | UserNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    public void update() {
        try {
            System.out.println("Enter the user id to find");
            Integer id = Integer.parseInt(SCANNER.nextLine());
            User userFoundById = userDao.findById(id).orElseThrow(() -> new InvalidIdException("The id is invalid"));
            log.info("ID:[{}] - {} - {} - {} - {}", userFoundById.getId(), userFoundById.getName(), userFoundById.getEmail(), userFoundById.getPhone(), userFoundById.getAddress());

            System.out.print("Enter the new user name or empty to keep the same: ");
            String name = SCANNER.nextLine();
            System.out.print("Enter the new user email or empty to keep the same: ");
            String email = SCANNER.nextLine();
            System.out.print("Enter the new user phone or empty to keep the same: ");
            String phone = SCANNER.nextLine();
            System.out.print("Enter the new user address or empty to keep the same: (STREET/CITY/STATE): ");
            String address = SCANNER.nextLine();

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
        } catch (NumberFormatException | UserNotFoundException e) {
            log.error(e.getMessage());
        }
    }
    public void findByName() {
        try {
            System.out.println("Enter the user name to found");
            String name = SCANNER.nextLine();
            List<User> userByName = userDao.findByName(name);
            log.info("User found '{}'", userByName);
        }catch (UserNotFoundException e){
           log.error(e.getMessage());
        }
    }
    public void findUserWithTheMostLoans(){
        log.info("User found with the most loans: ");
        userDao.userWithMostLoans().ifPresent(u -> System.out.printf("ID:[%d] - %s, %s, %s, %s %n", u.getId(), u.getName(), u.getEmail(), u.getPhone(), u.getAddress()));
    }
}
