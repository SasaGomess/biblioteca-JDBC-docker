package br.com.sabrinaweb.appbiblioteca.model.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Author {
    private Integer id;
    private String name;
    private LocalDate birthdate;
    private String nationality;
}
