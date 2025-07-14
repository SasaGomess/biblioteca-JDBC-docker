package br.com.sabrinaweb.appbiblioteca.model.dao;

import br.com.sabrinaweb.appbiblioteca.model.entities.Book;
import br.com.sabrinaweb.appbiblioteca.model.entities.LibraryLoan;
import br.com.sabrinaweb.appbiblioteca.model.entities.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LibraryLoanDao {
    void insert(LibraryLoan libraryLoan) throws SQLException;
    void update(LibraryLoan libraryLoan);
    void deleteById(Integer idLoan);
    List<LibraryLoan> findAllLoan();
    int isLoanBookAvailable(Integer idBook);
    Optional<LibraryLoan> findById(Integer id);
    Map<Integer, Book> findBooksBorrowedByStatus(String status);
    Map<List<Integer>, User> findUsersWithMoreThanOneBookBorrowed();
}
