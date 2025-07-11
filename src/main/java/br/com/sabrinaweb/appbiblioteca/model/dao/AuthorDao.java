package br.com.sabrinaweb.appbiblioteca.model.dao;

import br.com.sabrinaweb.appbiblioteca.model.entities.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    void insert(Author autor);
    void deleteById(Integer id);
    void update(Author autor);
    List<Author> findAllAutors();
    List<Author> findByName(String name);
    Optional<Author> findById(Integer id);
    List<Author> findAutorByWroteBook(Integer idBook);
}
