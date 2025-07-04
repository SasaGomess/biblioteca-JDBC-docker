package br.com.sabrinaweb.appbiblioteca.model.dao.impl;

import br.com.sabrinaweb.appbiblioteca.model.dao.UserDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class UserDaoJdbc implements UserDao {
    private Connection conn;

    public UserDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(User user) {
        String sql = "INSERT INTO library.user (name, email, phone, address) VALUES (?, ?, ?, ?);";
        log.info("Saving the user '{}'", user.getName());

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getAddress());

            ps.execute();
        } catch (SQLException e) {
            log.error("Error trying to insert the user");

        }
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM library.user WHERE (id_user = ?);";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.execute();
        } catch (SQLException e) {
            log.error("Error trying to delete User by id '{}'", id);
        }
    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }

    @Override
    public List<User> findByName(String name) {
        return List.of();
    }
}
