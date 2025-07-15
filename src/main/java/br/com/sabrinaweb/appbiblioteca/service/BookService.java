package br.com.sabrinaweb.appbiblioteca.service;

import br.com.sabrinaweb.appbiblioteca.model.dao.BookDao;
import br.com.sabrinaweb.appbiblioteca.model.dao.DaoFactory;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.InvalidIdException;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Scanner;

@Log4j2
public class BookService {
    private static final Scanner SCANNER = new Scanner(System.in);

    private final BookDao bookDao = DaoFactory.createBookDao();

    public void insert(){
        System.out.println("Enter the book title to insert");
        String title = SCANNER.nextLine();
        System.out.println("Enter the book publisher: ");
        String publisher = SCANNER.nextLine();
        System.out.println("Enter the last digit of book isbn: ");
        String isbn = SCANNER.nextLine();
        System.out.println("Enter the book genre: ");
        String genre = SCANNER.nextLine();
        System.out.println("Enter the book year of publication: ");
        Integer yearOfPublication = Integer.parseInt(SCANNER.nextLine());
        System.out.println("Enter the book number of pages: ");
        int numberOfPages = Integer.parseInt(SCANNER.nextLine());
        Book book = Book.builder().title(title).publisher(publisher).genre(genre).isbn(isbn).year_public(yearOfPublication).numberPages(numberOfPages).build();
        bookDao.insert(book);
    }
    public void delete(){
        try {
            bookDao.findAllBooks().forEach(b -> System.out.printf("ID:[%d] - %s, %s, %s, %s %d %n", b.getId(), b.getTitle(), b.getIsbn(), b.getGenre(), b.getPublisher(), b.getNumberPages()));

            System.out.println("Enter the book id to delete");
            Integer id = Integer.parseInt(SCANNER.nextLine());

            if (id < 0) throw new InvalidIdException("The id is null or equal 0, you should enter a valid id");

            System.out.printf("Are you sure that you want to delete the book: '%d' [Y/N] %n", id);
            String resp = SCANNER.nextLine();
            if (resp.equalsIgnoreCase("y")) bookDao.deleteById(id);
        }catch (NumberFormatException e){
            log.error(e.getMessage());
        }
    }

    public void update(){
        try {
            System.out.println("Enter the book id to find");
            Integer id = Integer.parseInt(SCANNER.nextLine());
            Book bookFoundById = bookDao.findById(id).orElseThrow(() -> new InvalidIdException("The id is invalid"));
            log.info("ID:[{}] - {} - {} - {}", bookFoundById.getId(), bookFoundById.getTitle(), bookFoundById.getIsbn(), bookFoundById.getGenre());

            System.out.print("Enter the new book title or empty to keep the same");
            String title = SCANNER.nextLine();
            System.out.print("Enter the new book publisher or empty to keep the same: ");
            String publisher = SCANNER.nextLine();
            System.out.print("Enter the new book year of publication: ");
            Integer yearOfPublication = Integer.parseInt(SCANNER.nextLine());

            title = title.isEmpty() ? bookFoundById.getTitle() : title;
            publisher = publisher.isEmpty() ? bookFoundById.getPublisher() : publisher;

            Book bookToUpdate = Book.builder()
                    .id(bookFoundById.getId())
                    .title(title)
                    .publisher(publisher)
                    .year_public(yearOfPublication)
                    .build();
            bookDao.update(bookToUpdate);
        }catch (NumberFormatException e){
            log.error(e.getMessage());
        }

    }

    public void findByTitle(){
        System.out.println("Enter the book title to found");
        String title = SCANNER.nextLine();
        List<Book> userByName = bookDao.findByTitle(title);
        log.info("Book found '{}'", userByName);
    }

    public void findAvailableBooks(){
        List<Book> availableBooks = bookDao.findAvailableBooks();
        log.info("The available books found: ");
        availableBooks.forEach(System.out::println);
    }

    public void findBookOfAAuthor(){
        try {
            System.out.print("Enter the author id to find their book: ");
            int idAuthor = Integer.parseInt(SCANNER.nextLine());

            if (idAuthor < 0) throw new InvalidIdException("The id is null or equal 0, you should enter a valid id");

            List<Book> allBooksOfAAuthor = bookDao.findAllBooksOfAAuthor(idAuthor);
            log.info("Book(s) found '{}'", allBooksOfAAuthor);
        }catch (NumberFormatException e){
            log.error(e.getMessage());
        }
    }
    public void bookMoreBorrowed(){
        log.info("Book more borrowed: ");
        bookDao.bookMoreBorrowed().ifPresent(System.out::println);
    }
}
