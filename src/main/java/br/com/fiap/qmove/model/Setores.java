package br.com.fiap.qmove.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Setores {
    @Id
    private long id;
    private String nome;
    private String codigo;
}
