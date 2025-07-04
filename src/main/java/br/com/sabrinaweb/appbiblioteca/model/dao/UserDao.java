package br.com.sabrinaweb.appbiblioteca.model.dao;

import br.com.sabrinaweb.appbiblioteca.model.entities.User;

import java.util.List;

public interface UserDao {
    void insert(User user);
    void update(User user);
    void deleteById(Integer id);
    List<User> findAllUsers();
    List<User> findByName();
}
