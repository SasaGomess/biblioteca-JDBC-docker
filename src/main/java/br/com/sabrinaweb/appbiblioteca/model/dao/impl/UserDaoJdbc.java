package br.com.sabrinaweb.appbiblioteca.model.dao.impl;

import br.com.sabrinaweb.appbiblioteca.model.dao.UserDao;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;

import java.sql.Connection;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private Connection conn;

    public UserDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(User user) {
        
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }

    @Override
    public List<User> findByName() {
        return List.of();
    }
}
