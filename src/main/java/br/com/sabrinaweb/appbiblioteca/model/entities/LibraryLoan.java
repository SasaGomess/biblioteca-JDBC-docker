package br.com.sabrinaweb.appbiblioteca.model.entities;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class LibraryLoan {
    private Integer id;
    private Book book;
    private User user;
    private String status;
    private Date dueDate;
    private Date loanDate;
    private Date returnDate;
}
