package br.com.sabrinaweb.appbiblioteca.model.service;

import br.com.sabrinaweb.appbiblioteca.model.dao.BookDao;
import br.com.sabrinaweb.appbiblioteca.model.dao.DaoFactory;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.exceptions.InvalidIdException;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Scanner;

@Log4j2
public class BookService {
    private Scanner scanner;
    private BookDao bookDao;

    public BookService(Scanner scanner, BookDao bookDao) {
        this.scanner = scanner;
        this.bookDao = bookDao;
    }

    public BookService() {
    }

    public void insert() {
        System.out.println("Enter the book title to insert");
        String title = scanner.nextLine();
        System.out.println("Enter the book publisher: ");
        String publisher = scanner.nextLine();
        System.out.println("Enter the last digit of book isbn: ");
        String isbn = scanner.nextLine();
        System.out.println("Enter the book genre: ");
        String genre = scanner.nextLine();
        System.out.println("Enter the book year of publication: ");
        Integer yearOfPublication = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter the book number of pages: ");
        int numberOfPages = Integer.parseInt(scanner.nextLine());

        if (title.isEmpty() || publisher.isEmpty() || isbn.isEmpty() || genre.isEmpty())
            throw new IllegalArgumentException("You need to enter all fields to insert a valid book, otherwise you won't be able to insert the book");
        Book book = Book.builder()
                .title(title)
                .publisher(publisher)
                .genre(genre).isbn(isbn)
                .year_public(yearOfPublication)
                .numberPages(numberOfPages)
                .build();
        bookDao.insert(book);
    }

    public void delete() {

        bookDao.findAllBooks().forEach(b -> System.out.printf("ID:[%d] - %s, %s, %s, %s, %d %n", b.getId(), b.getTitle(), b.getIsbn(), b.getGenre(), b.getPublisher(), b.getNumberPages()));

        System.out.println("Enter the book id to delete");
        Integer id = Integer.parseInt(scanner.nextLine());

        invalidId(id);
        System.out.printf("Are you sure that you want to delete the book: '%d' [Y/N] %n", id);
        String resp = scanner.nextLine();
        if (resp.equalsIgnoreCase("y")) bookDao.deleteById(id);
    }

    public void update() {

        System.out.println("Enter the book id to find");
        Integer id = Integer.parseInt(scanner.nextLine());
        invalidId(id);
        Book bookFoundById = bookDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Could not find the book by the specified id"));
        log.info("ID:[{}] - {} - {} - {}", bookFoundById.getId(), bookFoundById.getTitle(), bookFoundById.getIsbn(), bookFoundById.getGenre());

        System.out.print("Enter the new book title or empty to keep the same");
        String title = scanner.nextLine();
        System.out.print("Enter the new book publisher or empty to keep the same: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter the new book year of publication: ");
        Integer yearOfPublication = Integer.parseInt(scanner.nextLine());

        title = title.isEmpty() ? bookFoundById.getTitle() : title;
        publisher = publisher.isEmpty() ? bookFoundById.getPublisher() : publisher;

        Book bookToUpdate = Book.builder()
                .id(bookFoundById.getId())
                .title(title)
                .publisher(publisher)
                .year_public(yearOfPublication)
                .build();
        bookDao.update(bookToUpdate);
    }

    public void findByTitle() {
        System.out.println("Enter the book title to found");
        String title = scanner.nextLine();
        List<Book> userByName = bookDao.findByTitle(title);
        log.info("Book found '{}'", userByName);
    }

    public void findAvailableBooks() {
        List<Book> availableBooks = bookDao.findAvailableBooks();
        log.info("The available books found: ");
        availableBooks.forEach(System.out::println);
    }

    public void findBookOfAAuthor() {

        System.out.print("Enter the author id to find their book: ");
        int idAuthor = Integer.parseInt(scanner.nextLine());

        invalidId(idAuthor);

        List<Book> allBooksOfAAuthor = bookDao.findAllBooksOfAAuthor(idAuthor);
        log.info("Book(s) found '{}'", allBooksOfAAuthor);
    }

    public void theMostBorrowedBook() {
        log.info("The most borrowed book: ");
        bookDao.bookMoreBorrowed().ifPresent(b -> System.out.printf("ID:[%d] - %s, %s, %s, %s, %d %n", b.getId(), b.getTitle(), b.getIsbn(), b.getGenre(), b.getPublisher(), b.getNumberPages()));
    }

    public void invalidId(Integer id) {
        if (id <= 0) throw new InvalidIdException("The id is lower or equal 0, you should enter a valid id");
    }
}
