package br.com.fiap.qmove.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O campo 'nome' é obrigatório")
    private String nome;
    
    @NotNull(message = "O campo 'codigo é obrigatório")
    private String codigo;

    @ManyToMany
    @JoinTable(
        name = "setor_moto",
        joinColumns = @JoinColumn(name = "setor_id"),
        inverseJoinColumns = @JoinColumn(name = "moto_id")
    )
    @JsonIgnore
        private List<Moto> motos = new ArrayList<>();  // <-- INICIALIZA AQUI

    @OneToMany(mappedBy = "setor", cascade = CascadeType.ALL)
    private List<Alerta> alertas;
}
