package br.com.sabrinaweb.appbiblioteca.model.dao.impl;

import br.com.sabrinaweb.appbiblioteca.model.dao.AuthorDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Author;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class AuthorDaoJdbc implements AuthorDao {
    private Connection conn;

    public AuthorDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Author author) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO `library`.`author` (name, birthdate, nationality) VALUES (?, ?, ?)")) {
            ps.setString(1, author.getName());
            ps.setDate(2, Date.valueOf(author.getBirthdate()));
            ps.setString(3, author.getNationality());

            ps.execute();
        } catch (SQLException e) {
            log.error("Error trying to insert the author '{}'", author.getName());
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM `library`.`author` WHERE (id_author = ?)")) {
            ps.setInt(1, id);
        } catch (SQLException e) {
            log.error("Error trying to delete the author by id '{}'", id);
        }
    }

    @Override
    public void update(Author author) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE `library`.`author` SET name = ?, birthdate = ?, nationality = ? WHERE id_autor = ?")) {
            ps.setString(1, author.getName());
            ps.setDate(2, Date.valueOf(author.getBirthdate()));
            ps.setString(3, author.getNationality());
            ps.setInt(4, author.getId());

            ps.execute();
        } catch (SQLException e) {
            log.error("Error trying to update the author '{}', where the id is '{}'", author.getName(), author.getId());
        }
    }

    @Override
    public List<Author> findAllAutors() {
        List<Author> autors = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM `library`.`author`;");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                autors.add(Author.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id_autor"))
                        .birthdate(rs.getDate("birthdate").toLocalDate())
                        .nationality(rs.getString("nationality"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Error trying to find all authors");
        }
        return autors;
    }

    @Override
    public List<Author> findByName(String name) {
        List<Author> autors = new ArrayList<>();
        try (PreparedStatement ps = findByNamePreparedStatement(name);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                autors.add(Author.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id_autor"))
                        .birthdate(rs.getDate("birthdate").toLocalDate())
                        .nationality(rs.getString("nationality"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Error trying to find author by name '{}'", name);
        }
        return autors;
    }

    @Override
    public Optional<Author> findById(Integer id) {
        try (PreparedStatement ps = findByIdPreparedStatement(id);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return Optional.of(Author.builder()
                        .id(rs.getInt("id_autor"))
                        .name(rs.getString("name"))
                        .birthdate(rs.getDate("birthdate").toLocalDate())
                        .nationality(rs.getString("nationality"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Error trying to find the author by id '{}'", id);
        }
        return Optional.empty();
    }

    @Override
    public List<Author> findAutorByWroteBook(Integer idBook) {
        List<Author> autors = new ArrayList<>();
        try (PreparedStatement ps = findAuthorByBookIdPreparedStatement(idBook);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()){
                autors.add(Author.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id_autor"))
                        .birthdate(rs.getDate("birthdate").toLocalDate())
                        .nationality(rs.getString("nationality"))
                        .build());
            }

        } catch (SQLException e) {
            log.error("Error trying to find the author(s) of the book '{}'", idBook);
        }
        return autors;
    }
    private PreparedStatement findByNamePreparedStatement(String name) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM `library`.`author` WHERE name LIKE ?;");
        ps.setString(1, String.format("%%%s%%", name));
        return ps;
    }
    private PreparedStatement findByIdPreparedStatement(Integer id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM `library`.`author` WHERE id_author = ?;");
        ps.setInt(1, id);
        return ps;
    }
    private PreparedStatement findByBookIdPreparedStatement(Integer idBook) throws SQLException {
        String sql = "SELECT au.* from library.author AS au" +
                "INNER JOIN library.book_autor AS ba ON au.id_author = ba.id_autor" +
                "INNER JOIN library.book AS bo ON ba.id_book = bo.id_book WHERE ba.id_book = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idBook);
        return ps;
    }
}
