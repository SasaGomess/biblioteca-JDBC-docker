package br.com.sabrinaweb.appbiblioteca.model.dao;

import br.com.sabrinaweb.appbiblioteca.model.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    void insert(Book book);
    void update(Book book);
    void deleteById(Integer id);
    List<Book> findAllBooks();
    List<Book> findByTitle(String name);
    List<Book> findAvailableBooks();
    List<Book> findAllBooksOfAAuthor(Integer id_author);
    Optional<Book> findById(Integer id);
    Optional<Book> bookMoreBorrowed();
}
