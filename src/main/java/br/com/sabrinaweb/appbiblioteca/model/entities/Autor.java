package br.com.sabrinaweb.appbiblioteca.model.entities;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Autor {
    private Integer id;
    private String name;
    private Date birthdate;
    private String nationality;
}
