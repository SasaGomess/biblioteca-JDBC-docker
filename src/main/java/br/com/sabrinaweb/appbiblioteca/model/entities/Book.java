package br.com.sabrinaweb.appbiblioteca.model.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {
    private Integer id;
    private String isbn;
    private String title;
    private Integer year_public;
    private String publisher;
    private String genre;
    private int numberPages;
}
