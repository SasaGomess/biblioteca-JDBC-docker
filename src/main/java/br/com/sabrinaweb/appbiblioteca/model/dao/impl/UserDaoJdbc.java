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
import java.util.Optional;

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

        try (PreparedStatement st = conn.prepareStatement("DELETE FROM library.user WHERE (id_user = ?);")) {
            st.setInt(1, id);
            st.execute();
        } catch (SQLException e) {
            log.error("Error trying to delete User by id '{}'", id);
        }
    }

    @Override
    public List<User> findAllUsers() {
        log.info("Finding all users");
        List<User> user = new ArrayList<>();

        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM library.user;");
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                user.add(User.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id_user"))
                        .email(rs.getString("email"))
                        .phone(rs.getNString("phone"))
                        .address(rs.getString("address"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Error while trying to find all users");
        }
        return user;
    }

    @Override
    public List<User> findByName(String name) {
        List<User> users = new ArrayList<>();
        try (PreparedStatement ps = findByNamePreparedStatement(name);
            ResultSet rs = ps.executeQuery();){

            while (rs.next()){
                users.add(User.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id_user"))
                        .email(rs.getString("email"))
                        .phone(rs.getNString("phone"))
                        .address(rs.getString("address"))
                        .build());
            }

        }catch (SQLException e){
            log.error("Error trying to find user by '{}' name", name);
        }
        return users;
    }

    @Override
    public Optional<User> findById(Integer id) {
        log.info("Finding user by id '{}'", id);
        try (PreparedStatement ps = findByIdPreparedStatement(id);
             ResultSet rs = ps.executeQuery()){
            if (rs.next()){
                return Optional.of(User.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id_user"))
                        .email(rs.getString("email"))
                        .phone(rs.getNString("phone"))
                        .address(rs.getString("address"))
                        .build());
            }

        }catch (SQLException e){
            log.error("Error trying to find user by the '{}' id", id, e);
        }
        return Optional.empty();
    }

    private PreparedStatement findByIdPreparedStatement(Integer id) throws SQLException {
        String sql = "SELECT * FROM library.user WHERE (id_user = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }
    private PreparedStatement findByNamePreparedStatement(String name) throws SQLException{
        String sql = "SELECT * FROM library.user WHERE name LIKE ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", name));
        return ps;
    }
}
