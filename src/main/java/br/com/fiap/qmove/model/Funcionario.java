package br.com.fiap.qmove.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Funcionario {
    @Id
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private Setor setor;

}
