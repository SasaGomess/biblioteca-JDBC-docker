package br.com.sabrinaweb.appbiblioteca.model.dao.impl;

import br.com.sabrinaweb.appbiblioteca.model.dao.LibraryLoanDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.entities.LibraryLoan;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LibraryLoanDaoJdbc implements LibraryLoanDao {

    private Connection conn;

    public LibraryLoanDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(LibraryLoan libraryLoan) {

    }

    @Override
    public void update(LibraryLoan libraryLoan) {

    }

    @Override
    public void delete(Integer idLoan) {

    }

    @Override
    public List<LibraryLoan> findAllLoan() {
        return List.of();
    }

    @Override
    public int isLoanBookAvailable(Integer idBook) {
        return 0;
    }

    @Override
    public Optional<LibraryLoan> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Map<Integer, Book> findBooksBorrowedByStatus(String status) {
        return Map.of();
    }

    @Override
    public Map<Integer, User> findUsersWithMoreThanOneBookBorrowed() {
        return Map.of();
    }
}
