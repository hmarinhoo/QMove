package br.com.fiap.qmove.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Motos {
    private Long id;
    private String placa;
    private String modelo;
    private String status;
}
