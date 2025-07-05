package br.com.sabrinaweb.appbiblioteca.model.dao.impl;

import br.com.sabrinaweb.appbiblioteca.model.dao.BookDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class BookDaoJdbc implements BookDao {
    private Connection conn;

    public BookDaoJdbc(Connection conn) {
        this.conn = conn;
    }

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
        List<Book> books = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM library.book");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                books.add(Book.builder()
                        .isbn(rs.getString("isbn"))
                        .id(rs.getInt("id_book"))
                        .genre(rs.getString("genre"))
                        .title(rs.getString("title"))
                        .publisher(rs.getString("publisher"))
                        .year_public(rs.getInt("year_public"))
                        .numberPages(rs.getInt("number_pages"))
                        .build());
            }

        } catch (SQLException e) {
            log.error("Error while trying to find all books");
        }
        return books;
    }

    @Override
    public List<Book> findByTitle(String title) {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement ps = findByTitlePreparedStatement(title);
             ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                books.add(Book.builder()
                        .isbn(rs.getString("isbn"))
                        .id(rs.getInt("id_book"))
                        .genre(rs.getString("genre"))
                        .title(rs.getString("title"))
                        .publisher(rs.getString("publisher"))
                        .year_public(rs.getInt("year_public"))
                        .numberPages(rs.getInt("number_pages"))
                        .build());
            }
        }catch (SQLException e){
            log.error("Error trying to find book by '{}' name", title);
        }
        return books;
    }

    private PreparedStatement findByTitlePreparedStatement(String title) throws SQLException {
        String sql = "SELECT * FROM library.book WHERE title LIKE ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", title));
        return ps;
    }

    @Override
    public List<Book> findAvailableBooks(String name) {
        String sql = "SELECT * FROM library.book WHERE id_book NOT IN (SELECT l.id_book FROM library_loan AS l);";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            if (!rs.next()) return books;

            while (rs.next()){
                books.add(Book.builder()
                        .isbn(rs.getString("isbn"))
                        .id(rs.getInt("id_book"))
                        .genre(rs.getString("genre"))
                        .title(rs.getString("title"))
                        .publisher(rs.getString("publisher"))
                        .year_public(rs.getInt("year_public"))
                        .numberPages(rs.getInt("number_pages"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Error trying to found available books");
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Integer id) {
        return Optional.empty();
    }
}
