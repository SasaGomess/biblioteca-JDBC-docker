package br.com.sabrinaweb.appbiblioteca.model.dao.impl;

import br.com.sabrinaweb.appbiblioteca.model.dao.LibraryLoanDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.entities.LibraryLoan;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
public class LibraryLoanDaoJdbc implements LibraryLoanDao {

    private Connection conn;

    public LibraryLoanDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(LibraryLoan libraryLoan) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO `library`.`library_loan` (id_book, id_user, status, loan_date, due_date) VALUES (?, ?, ?, ?, ?)")) {
            conn.setAutoCommit(false);
            ps.setInt(1, libraryLoan.getBook().getId());
            ps.setInt(2, libraryLoan.getUser().getId());
            ps.setString(3, libraryLoan.getStatus());
            ps.setDate(4, Date.valueOf(libraryLoan.getLoanDate()));
            ps.setDate(5, Date.valueOf(libraryLoan.getDueDate()));

            ps.execute();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            log.error("Error trying to register the new loan");
        }
    }

    @Override
    public void update(LibraryLoan libraryLoan) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE `library`.`library_loan` SET status = ? returnDate = ? WHERE id_loan = ? ")) {
            ps.setString(1, libraryLoan.getStatus());
            ps.setDate(2, Date.valueOf(libraryLoan.getReturnDate()));
            ps.setInt(3, libraryLoan.getId());

            ps.execute();
        } catch (SQLException e) {
            log.error("Error trying to update the loan service");
        }
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
