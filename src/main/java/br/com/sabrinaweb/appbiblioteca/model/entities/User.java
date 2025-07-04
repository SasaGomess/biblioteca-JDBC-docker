package br.com.sabrinaweb.appbiblioteca.model.entities;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class User {
    private Integer id;
    private String name;
    private String email;
    private String address;
    private String phone;
}
