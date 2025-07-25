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
    private final Connection conn;

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
            ps.setString(4, String.format("978-85-0000000-%s", book.getIsbn()));
            ps.setInt(5, book.getNumberPages());
            ps.setInt(6, book.getYear_public());
            ps.execute();
            log.info("The book was registered with success!");
        } catch (SQLException e) {
            log.error("Error trying to insert the book '{}'", book.getTitle());
        }
    }

    @Override
    public void update(Book book) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE library.book SET title = ?, year_public = ?, publisher = ?, genre = ? WHERE id_book = ?;")) {
            ps.setString(1, book.getTitle());
            ps.setInt(2, book.getYear_public());
            ps.setString(3, book.getPublisher());
            ps.setInt(4, book.getId());

            ps.execute();
            log.info("The book was updated with success!");
        } catch (SQLException e) {
            log.error("Error trying to update the book '{}'", book.getTitle());
        }
    }

    @Override
    public void deleteById(Integer idBook) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM library.book WHERE (id_book = ?)")) {
            ps.setInt(1, idBook);
            ps.execute();
            log.info("The book was deleted with success");
        } catch (SQLException e) {
            log.error("Error trying to delete book with the id '{}'", idBook);
        }
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
                        .status(rs.getString("status"))
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
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                books.add(Book.builder()
                        .isbn(rs.getString("isbn"))
                        .id(rs.getInt("id_book"))
                        .genre(rs.getString("genre"))
                        .title(rs.getString("title"))
                        .publisher(rs.getString("publisher"))
                        .year_public(rs.getInt("year_public"))
                        .status(rs.getString("status"))
                        .numberPages(rs.getInt("number_pages"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Error trying to find book by '{}' title", title);
        }
        return books;
    }

    @Override
    public List<Book> findAvailableBooks() {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM library.book WHERE status = 'disponível';");
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                books.add(Book.builder()
                        .isbn(rs.getString("isbn"))
                        .id(rs.getInt("id_book"))
                        .genre(rs.getString("genre"))
                        .title(rs.getString("title"))
                        .publisher(rs.getString("publisher"))
                        .year_public(rs.getInt("year_public"))
                        .status(rs.getString("status"))
                        .numberPages(rs.getInt("number_pages"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Error trying to found available books any book are available in the moment");
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Integer idBook) {
        try (PreparedStatement ps = findByIdPreparedStatement(idBook);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return Optional.of(Book.builder()
                        .isbn(rs.getString("isbn"))
                        .id(rs.getInt("id_book"))
                        .genre(rs.getString("genre"))
                        .title(rs.getString("title"))
                        .status(rs.getString("status"))
                        .publisher(rs.getString("publisher"))
                        .year_public(rs.getInt("year_public"))
                        .numberPages(rs.getInt("number_pages"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Error trying to find the book by id '{}'", idBook);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAllBooksOfAAuthor(Integer idAuthor) {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement ps = findBookByAutorIdPreparedStatement(idAuthor);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                books.add(Book.builder()
                        .isbn(rs.getString("isbn"))
                        .title(rs.getString("title"))
                        .publisher(rs.getString("publisher"))
                        .year_public(rs.getInt("year_public"))
                        .status(rs.getString("status"))
                        .numberPages(rs.getInt("number_pages"))
                        .id(rs.getInt("id_book"))
                        .genre(rs.getString("genre"))
                        .build());
            }

        } catch (SQLException e) {
            log.error("Error trying to find the books of the author '{}'", idAuthor);
        }
        return books;
    }
    @Override
    public Optional<Book> bookMoreBorrowed() {
        try (PreparedStatement ps = conn.prepareStatement("SELECT bo.*, COUNT(ll.id_book) AS book_more_borrowed FROM library.book AS bo INNER JOIN library.library_loan AS ll ON ll.id_book = bo.id_book GROUP BY ll.id_book ORDER BY book_more_borrowed DESC LIMIT 1;");
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return Optional.of(Book.builder()
                        .id(rs.getInt("id_book"))
                        .title(rs.getString("title"))
                        .genre(rs.getString("genre"))
                        .status(rs.getString("status"))
                        .year_public(rs.getInt("year_public"))
                        .isbn(rs.getString("isbn"))
                        .publisher(rs.getString("publisher"))
                        .numberPages(rs.getInt("number_pages"))
                        .build());
            }

        } catch (SQLException e) {
            log.error("Error while trying to found the book more borrowed");
        }
        return Optional.empty();
    }

    private PreparedStatement findBookByAutorIdPreparedStatement(Integer idAuthor) throws SQLException {
        String sql = "SELECT bo.* from library.author AS au " +
                "INNER JOIN library.book_author AS ba ON au.id_author = ba.id_author" +
                "INNER JOIN library.book AS bo ON ba.id_book = bo.id_book WHERE ba.id_author = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idAuthor);
        return ps;
    }

    private PreparedStatement findByIdPreparedStatement(Integer id) throws SQLException {
        String sql = "SELECT * FROM library.book WHERE (id_book = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

    private PreparedStatement findByTitlePreparedStatement(String title) throws SQLException {
        String sql = "SELECT * FROM library.book WHERE title LIKE ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", title));
        return ps;
    }
}
