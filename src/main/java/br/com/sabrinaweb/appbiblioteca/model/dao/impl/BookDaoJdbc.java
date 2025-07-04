package br.com.sabrinaweb.appbiblioteca.model.dao.impl;

import br.com.sabrinaweb.appbiblioteca.model.dao.BookDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Log4j2
public class BookDaoJdbc implements BookDao {
    private Connection conn;


    @Override
    public void insert(Book book) {
        String sql = "INSERT INTO library.book (title, genre, publisher, isbn, number_pages, year_public) VALUES (?, ?, ?, ?, ?, ?);";
        log.info("Saving the book '{}'", book.getTitle());

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getGenre());
            ps.setString(3, book.getPublisher());
            ps.setString(4, book.getIsbn());
            ps.setInt(5, book.getNumberPages());
            ps.setDate(6, new Date(book.getYear_public()));
            ps.execute();
        } catch (SQLException e) {
            log.error("Error trying to insert the book '{}'", book.getTitle());
        }
    }
    @Override
    public void update(Book book) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public List<Book> findAllBooks() {
        return List.of();
    }

    @Override
    public List<Book> findByTitle(String name) {
        return List.of();
    }

    @Override
    public List<Book> findAvailableBooks(String name) {
        return List.of();
    }

    @Override
    public Optional<Book> findById(Integer id) {
        return Optional.empty();
    }
}
