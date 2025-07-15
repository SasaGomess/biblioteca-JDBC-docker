package br.com.sabrinaweb.appbiblioteca.model.dao;

import br.com.sabrinaweb.appbiblioteca.model.entities.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    void insert(Author author);
    void deleteById(Integer id);
    void update(Author author);
    List<Author> findAllAuthors();
    List<Author> findByName(String name);
    Optional<Author> findById(Integer id);
    List<Author> findAuthorByWroteBook(Integer idBook);
}
