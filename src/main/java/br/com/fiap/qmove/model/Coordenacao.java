package br.com.fiap.qmove.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Coordenacao {
    private Long id;
    private String email;
    private String senha;
}
