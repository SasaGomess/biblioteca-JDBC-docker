package br.com.sabrinaweb.appbiblioteca.model.dao.impl;

import br.com.sabrinaweb.appbiblioteca.model.dao.LibraryLoanDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.entities.LibraryLoan;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
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
    public void deleteById(Integer idLoan) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM `library`.`library_loan` WHERE id_loan = ?")) {
            ps.setInt(1, idLoan);
            ps.execute();
        } catch (SQLException e) {
            log.error("Error trying to delete the loan '{}'", idLoan);
        }
    }

    @Override
    public List<LibraryLoan> findAllLoan() {
        List<LibraryLoan> loans = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM `library`.`library_loan`;");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Book book = Book.builder().id(rs.getInt("id_book")).build();
                User user = User.builder().id(rs.getInt("id_user")).build();
                loans.add(LibraryLoan.builder().id(rs.getInt("id_loan"))
                        .book(book)
                        .user(user)
                        .status(rs.getString("status"))
                        .loanDate(rs.getDate("loan_date").toLocalDate())
                        .dueDate(rs.getDate("due_date").toLocalDate())
                        .returnDate(rs.getDate("return_date").toLocalDate())
                        .build());
            }
        } catch (SQLException e) {
            log.error("Error trying to find all loans");
        }
        return loans;
    }

    @Override
    public int isLoanBookAvailable(Integer idBook) {
        int value = 1;
        try (PreparedStatement ps = bookAvailablePreparedStatement(idBook);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) value = rs.getInt("isNotAvailable");

        } catch (SQLException e) {
            log.error("Error trying to verify if the book '{}' is available for loan", idBook);
        }
        return value;    }

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

    private PreparedStatement bookAvailablePreparedStatement(Integer idBook) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS isNotAvailable FROM `library`.`library_loan` WHERE id_book = ? AND return_date IS NULL;");
        ps.setInt(1, idBook);
        return ps;
    }
}
