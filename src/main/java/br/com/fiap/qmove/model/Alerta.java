package br.com.fiap.qmove.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.fiap.qmove.model.enums.Tipo;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    private String descricao;
    private LocalDateTime dataHora;
    private boolean lido;

    @ManyToOne
    @JoinColumn(name = "moto_id")
    private Moto moto;

    @ManyToOne
    @JoinColumn(name = "setor_id")
    @JsonIgnoreProperties("motos")
    private Setor setor;
}
