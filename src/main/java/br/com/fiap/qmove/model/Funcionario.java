package br.com.fiap.qmove.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Funcionario {
    private Long id;
    private String nome;
    private String email;
    private String senha;
}
