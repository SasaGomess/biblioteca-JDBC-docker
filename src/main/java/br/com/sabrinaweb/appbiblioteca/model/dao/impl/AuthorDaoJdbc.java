package br.com.sabrinaweb.appbiblioteca.model.dao.impl;

import br.com.sabrinaweb.appbiblioteca.model.dao.AuthorDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.Author;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        return List.of();
    }

    @Override
    public List<Author> findByName(String name) {
        return List.of();
    }

    @Override
    public Optional<Author> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Author> findAutorByWroteBook(Integer idBook) {
        return List.of();
    }
}
