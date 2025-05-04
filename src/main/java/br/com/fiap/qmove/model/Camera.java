package br.com.fiap.qmove.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Camera {
    private Long id;
    private String localizacao;
    private String ip;
    private String status;

    private Setores setores;
}
