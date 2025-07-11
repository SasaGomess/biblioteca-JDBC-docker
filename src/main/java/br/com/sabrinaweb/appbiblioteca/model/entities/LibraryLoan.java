package br.com.sabrinaweb.appbiblioteca.model.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class LibraryLoan {
    private Integer id;
    private Book book;
    private User user;
    private String status;
    private LocalDate dueDate;
    private LocalDate loanDate;
    private LocalDate returnDate;
}
