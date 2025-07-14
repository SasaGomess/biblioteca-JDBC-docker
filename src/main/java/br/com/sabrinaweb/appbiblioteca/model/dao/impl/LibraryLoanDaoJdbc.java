package br.com.sabrinaweb.appbiblioteca.model.dao.impl;

import br.com.sabrinaweb.appbiblioteca.model.dao.LibraryLoanDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.entities.LibraryLoan;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Log4j2
public class LibraryLoanDaoJdbc implements LibraryLoanDao {

    private final Connection conn;

    public LibraryLoanDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(LibraryLoan libraryLoan) throws SQLException {
        conn.setAutoCommit(false);
        try {
            PreparedStatement psInsert = conn.prepareStatement("INSERT INTO `library`.`library_loan` (id_book, id_user, status, loan_date, due_date) VALUES (?, ?, ?, ?, ?)");

            psInsert.setInt(1, libraryLoan.getBook().getId());
            psInsert.setInt(2, libraryLoan.getUser().getId());
            psInsert.setString(3, libraryLoan.getStatus());
            psInsert.setDate(4, Date.valueOf(libraryLoan.getLoanDate()));
            psInsert.setDate(5, Date.valueOf(libraryLoan.getDueDate()));

            PreparedStatement psUpdateStatus = conn.prepareStatement("UPDATE `library`.`book` SET status = ? WHERE id_book = ? AND status = 'disponível';");

            psUpdateStatus.setString(1, libraryLoan.getBook().getStatus());
            psUpdateStatus.setInt(2, libraryLoan.getBook().getId());

            psInsert.execute();
            psUpdateStatus.execute();

            conn.commit();
            log.info("The loan was inserted with success");
        } catch (SQLException e) {
            conn.rollback();
            log.error("Error trying to register the new loan");
        }
        conn.setAutoCommit(true);
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
        return value;
    }

    @Override
    public Optional<LibraryLoan> findById(Integer id) {
        try (PreparedStatement ps = findByIdPreparedStatement(id);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Book book = Book.builder().id(rs.getInt("id_book")).build();
                User user = User.builder().id(rs.getInt("id_user")).build();
                return Optional.of(LibraryLoan.builder()
                        .id(rs.getInt("id_loan"))
                        .book(book)
                        .user(user)
                        .status(rs.getString("status"))
                        .loanDate(rs.getDate("loan_date").toLocalDate())
                        .dueDate(rs.getDate("due_date").toLocalDate())
                        .returnDate(rs.getDate("return_date").toLocalDate())
                        .build());
            }
        } catch (SQLException e) {
            log.error("Error trying to find the loan by id '{}'", id);
        }
        return Optional.empty();
    }

    @Override
    public Map<Integer, Book> findBooksBorrowedByStatus(String status) {
        Map<Integer, Book> booksBorrowedFound = new HashMap<>();
        try (PreparedStatement ps = findBooksBorrowedByStatusPreparedStatement(status);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Integer idLoan = LibraryLoan.builder().id(rs.getInt("id_loan")).build().getId();
                Book book = Book.builder()
                        .isbn(rs.getString("isbn"))
                        .title(rs.getString("title"))
                        .publisher(rs.getString("publisher"))
                        .year_public(rs.getInt("year_public"))
                        .numberPages(rs.getInt("number_pages"))
                        .id(rs.getInt("id_book"))
                        .genre(rs.getString("genre"))
                        .build();

                booksBorrowedFound.put(idLoan, book);
            }
        } catch (SQLException e) {
            log.error("Error trying to find the book in the loan service by status");
        }
        return booksBorrowedFound;
    }

    @Override
    public Map<List<Integer>, User> findUsersWithMoreThanOneBookBorrowed() {

        Map<List<Integer>, User> usersWithMoreThanOneBooks = new HashMap<>();
        List<Integer> idsLoan = new ArrayList<>();
        try (PreparedStatement psUser = conn.prepareStatement("SELECT us.* FROM `library`.`user` AS us INNER JOIN `library`.`library_loan` AS ll ON us.id_user = ll.id_user GROUP BY ll.id_user HAVING COUNT(ll.id_user) > 1;");
             ResultSet rsUser = psUser.executeQuery()) {

            PreparedStatement psIdLoan = conn.prepareStatement("SELECT id_loan FROM `library`.`library_loan` WHERE id_user = ?;");

            while (rsUser.next()) {
                User user = User.builder()
                        .name(rsUser.getString("us.name"))
                        .id(rsUser.getInt("us.id_user"))
                        .email(rsUser.getString("us.email"))
                        .phone(rsUser.getNString("us.phone"))
                        .address(rsUser.getString("us.address"))
                        .build();

                psIdLoan.setInt(1, user.getId());
                ResultSet rsIdLoan = psIdLoan.executeQuery();
                while (rsIdLoan.next()){
                    idsLoan.add(rsIdLoan.getInt("id_loan"));
                }
                usersWithMoreThanOneBooks.put(idsLoan, user);
            }
        } catch (SQLException e) {
            log.error("Error trying to find the user with more than one book borrowed");
        }
        return usersWithMoreThanOneBooks;
    }

    private PreparedStatement bookAvailablePreparedStatement(Integer idBook) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS isNotAvailable FROM `library`.`library_loan` AS ll inner join `library`.`book` AS bo ON ll.id_book = bo.id_book WHERE ll.id_book = ? and ll.return_date IS NULL AND bo.status = 'indisponível';");
        ps.setInt(1, idBook);
        return ps;
    }

    private PreparedStatement findByIdPreparedStatement(Integer id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM `library`.`library_loan` WHERE id_loan = ?;");
        ps.setInt(1, id);
        return ps;
    }

    private PreparedStatement findBooksBorrowedByStatusPreparedStatement(String status) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT bo.*, ll.id_loan FROM library.book AS bo INNER JOIN library.library_loan AS ll ON ll.id_book = bo.id_book WHERE ll.status LIKE ? OR bo.status LIKE ?");
        ps.setString(1, String.format("%s%%", status));
        ps.setString(2, String.format("%s%%", status));
        ps.execute();
        return ps;
    }
}
