package br.com.sabrinaweb.appbiblioteca.model.service;

import br.com.sabrinaweb.appbiblioteca.model.dao.AuthorDao;
import br.com.sabrinaweb.appbiblioteca.model.dao.DaoFactory;
import br.com.sabrinaweb.appbiblioteca.model.entities.Author;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.InvalidIdException;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Log4j2
public class AuthorService {
    private Scanner scanner;
    private AuthorDao authorDao;

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AuthorService(Scanner scanner, AuthorDao authorDao) {
        this.scanner = scanner;
        this.authorDao = authorDao;
    }

    public AuthorService() {
    }

    public void insert() {
        System.out.println("Enter the author name to insert");
        String name = scanner.nextLine();
        System.out.println("Enter the nationality of the author: ");
        String nationality = scanner.nextLine();
        System.out.println("Enter the author birthdate: (yyyy-MM-dd)");
        String birthDate = scanner.nextLine();

        if (name.isEmpty() | nationality.isEmpty() | birthDate.isEmpty()) throw new IllegalArgumentException("You need to enter all fields to insert a valid author, otherwise you won't be able to insert the author");
        Author author = Author.builder().name(name).nationality(nationality).birthdate(LocalDate.parse(birthDate, fmt)).build();
        authorDao.insert(author);
    }

    public void delete() {
        authorDao.findAllAuthors().forEach(a -> System.out.printf("ID:[%d] - %s, %s, %s %n", a.getId(), a.getName(), a.getNationality(), a.getBirthdate()));

        System.out.println("Enter the author id to delete");
        Integer id = Integer.parseInt(scanner.nextLine());

        invalidId(id);

        System.out.printf("Are you sure that you want to delete the author: '%d' [Y/N] %n", id);
        System.out.print("RESP: ");
        String resp = scanner.nextLine();
        if (resp.equalsIgnoreCase("y")) authorDao.deleteById(id);
    }

    public void update() {
        System.out.println("Enter the author id to find");
        Integer id = Integer.parseInt(scanner.nextLine());
        invalidId(id);
        Author authorFoundById = authorDao.findById(id).orElseThrow(() -> new IllegalArgumentException("No author was found with the specified id"));
        log.info("ID:[{}] - {} - {} - {}", authorFoundById.getId(), authorFoundById.getName(), authorFoundById.getNationality(), authorFoundById.getBirthdate());
        System.out.println("Enter the new author name or empty to keep the same");
        String name = scanner.nextLine();
        System.out.println("Enter the new author nationality or empty to keep the same: ");
        String nationality = scanner.nextLine();
        System.out.println("Enter the author birthdate: (yyyy-mm-dd) or empty to keep the same");
        String birthDate = scanner.nextLine();

        name = name.isEmpty() ? authorFoundById.getName() : name;
        nationality = name.isEmpty() ? authorFoundById.getNationality() : nationality;
        birthDate = birthDate.isEmpty() ? authorFoundById.getBirthdate().toString() : birthDate;

        Author authorToUpdate = Author.builder()
                .id(authorFoundById.getId())
                .name(name)
                .nationality(nationality)
                .birthdate(LocalDate.parse(birthDate, fmt))
                .build();
        authorDao.update(authorToUpdate);
    }

    public void findByName() {
        System.out.println("Enter the author name to found");
        String name = scanner.nextLine();
        List<Author> authorByName = authorDao.findByName(name);
        log.info("Author(s) found '{}'", authorByName);
    }

    public void findAuthorByWroteBook() {
        System.out.print("Enter the book id to find its author(s): ");
        int id = Integer.parseInt(scanner.nextLine());
        invalidId(id);
        List<Author> authorsOfABook = authorDao.findAuthorByWroteBook(id);
        log.info("Author(s) found '{}'", authorsOfABook);
    }
    public void invalidId(Integer id) {
        if (id <= 0) throw new InvalidIdException("The id is lower or equal 0, you should enter a valid id");
    }
}
