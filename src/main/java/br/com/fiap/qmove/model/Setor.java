package br.com.fiap.qmove.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String codigo;

    @ManyToMany
    @JoinTable(
        name = "setor_moto",
        joinColumns = @JoinColumn(name = "setor_id"),
        inverseJoinColumns = @JoinColumn(name = "moto_id")
    )
    @JsonIgnore
    private List<Moto> motos;

    @OneToMany(mappedBy = "setor", cascade = CascadeType.ALL)
    private List<Alerta> alertas;
}
