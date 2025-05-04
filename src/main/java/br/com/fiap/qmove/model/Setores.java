package br.com.fiap.qmove.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Setores {
    private long id;
    private String nome;
    private String descricao;
}
