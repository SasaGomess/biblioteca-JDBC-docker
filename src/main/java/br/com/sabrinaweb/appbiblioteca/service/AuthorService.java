package br.com.sabrinaweb.appbiblioteca.service;

import br.com.sabrinaweb.appbiblioteca.model.dao.AuthorDao;
import br.com.sabrinaweb.appbiblioteca.model.dao.DaoFactory;
import br.com.sabrinaweb.appbiblioteca.model.entities.Author;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.InvalidIdException;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Log4j2
public class AuthorService {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final AuthorDao authorDao = DaoFactory.createAuthorDao();

    public void insert() {
        System.out.println("Enter the author name to insert");
        String name = SCANNER.nextLine();
        System.out.println("Enter the nationality of the author: ");
        String nationality = SCANNER.nextLine();
        System.out.println("Enter the author birthdate: (yyyy-MM-dd)");
        String birthDate = SCANNER.nextLine();

        Author author = Author.builder().name(name).nationality(nationality).birthdate(LocalDate.parse(birthDate, fmt)).build();
        authorDao.insert(author);
    }

    public void delete() {
        try{
            authorDao.findAllAutors().forEach(a -> System.out.printf("ID:[%d] - %s, %s, %s %n", a.getId(), a.getName(), a.getNationality(), a.getBirthdate()));

            System.out.println("Enter the author id to delete");
            Integer id = Integer.parseInt(SCANNER.nextLine());

            if (id < 0) throw new InvalidIdException("The id is null or equal 0, you should enter a valid id");

            System.out.printf("Are you sure that you want to delete the author: '%d' [Y/N] %n", id);
            System.out.print("RESP: ");
            String resp = SCANNER.nextLine();
            if (resp.equalsIgnoreCase("y")) authorDao.deleteById(id);
        }catch (NumberFormatException e){
            log.error(e.getMessage());
        }
    }

    public void update() {
        try {
            System.out.println("Enter the author id to find");
            Integer id = Integer.parseInt(SCANNER.nextLine());
            Author authorFoundById = authorDao.findById(id).orElseThrow(() -> new InvalidIdException("The id is invalid"));
            System.out.printf("ID:[%d] - %s, %s, %s %n", authorFoundById.getId(), authorFoundById.getName(), authorFoundById.getNationality(), authorFoundById.getBirthdate());
            System.out.println("Enter the new author name or empty to keep the same");
            String name = SCANNER.nextLine();
            System.out.println("Enter the new author nationality or empty to keep the same: ");
            String nationality = SCANNER.nextLine();
            System.out.println("Enter the author birthdate: (yyyy-mm-dd) or empty to keep the same");
            String birthDate = SCANNER.nextLine();

            name = name.isEmpty() ? authorFoundById.getName() : name;
            nationality = name.isEmpty() ? authorFoundById.getNationality() : nationality;
            birthDate = birthDate.isEmpty() ? authorFoundById.getBirthdate().toString() : birthDate;

            Author userToUpdate = Author.builder()
                    .id(authorFoundById.getId())
                    .name(name)
                    .nationality(nationality)
                    .birthdate(LocalDate.parse(birthDate, fmt))
                    .build();
            authorDao.update(userToUpdate);
            log.info("The author was updated with success");
        }catch (NumberFormatException e){
            log.error(e.getMessage());
        }
    }

    public void findByName() {
        System.out.println("Enter the author name to found");
        String name = SCANNER.nextLine();
        List<Author> userByName = authorDao.findByName(name);
        log.info("Author found '{}'", userByName);
    }

    public void findAuthorByWroteBook() {
        try {
            System.out.print("Enter the book id to find it's author (s): ");
            int id = Integer.parseInt(SCANNER.nextLine());
            if (id < 0) throw new InvalidIdException("The id is null or equal 0, you should enter a valid id");
            List<Author> allBooksOfAAuthor = authorDao.findAutorByWroteBook(id);
            log.info("Books found '{}'", allBooksOfAAuthor);

        }catch (NumberFormatException e){
            System.out.println(e.getMessage());
        }

    }
}
