package br.com.fiap.qmove.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Setor {
    private long id;
    private String nome;
    private String etiqueta;
}
