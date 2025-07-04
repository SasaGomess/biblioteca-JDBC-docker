package br.com.sabrinaweb.appbiblioteca.model.dao.impl;

import br.com.sabrinaweb.appbiblioteca.model.dao.BookDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class BookDaoJdbc implements BookDao {
    private Connection conn;


    @Override
    public void insert(Book book) {

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
