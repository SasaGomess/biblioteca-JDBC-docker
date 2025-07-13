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
        try (PreparedStatement ps = findByIdPreparedStatement(id);
             ResultSet rs = ps.executeQuery()){
            if (rs.next()){
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
        }catch (SQLException e){
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
    public Map<Integer, User> findUsersWithMoreThanOneBookBorrowed() {
        Map<Integer, User> usersWithMoreThanOneBooks = new HashMap<>();

        try (PreparedStatement ps = conn.prepareStatement("SELECT ll.id_loan, us.* FROM `library`.`user` AS us INNER JOIN `library`.`library_loan` AS ll ON us.id_user = ll.id_user GROUP BY ll.id_user HAVING COUNT(id_user) > 1");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Integer idLoan = LibraryLoan.builder().id(rs.getInt("id_loan")).build().getId();
                User user = User.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id_user"))
                        .email(rs.getString("email"))
                        .phone(rs.getNString("phone"))
                        .address(rs.getString("address"))
                        .build();
                usersWithMoreThanOneBooks.put(idLoan, user);
            }
        } catch (SQLException e) {
            log.error("Error trying to find the user with more than one book borrowed");
        }
        return usersWithMoreThanOneBooks;
    }

    private PreparedStatement bookAvailablePreparedStatement(Integer idBook) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS isNotAvailable FROM `library`.`library_loan` WHERE id_book = ? AND return_date IS NULL;");
        ps.setInt(1, idBook);
        return ps;
    }
    private PreparedStatement findByIdPreparedStatement(Integer id) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM `library`.`library_loan` WHERE id_loan = ?;");
        ps.setInt(1, id);
        return ps;
    }
    private PreparedStatement findBooksBorrowedByStatusPreparedStatement(String status) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT bo.*, ll.id_loan FROM library.book AS bo INNER JOIN library.library_loan AS ll ON ll.id_book = bo.id_book WHERE status LIKE ?");
        ps.setString(1, String.format("%%%s%%", status));
        ps.execute();
        return ps;
    }
}
