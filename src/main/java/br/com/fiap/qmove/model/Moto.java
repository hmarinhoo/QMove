package br.com.fiap.qmove.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Moto {
    @Id
    private Long id;

    @NotBlank(message = "O campo 'placa' é obrigatório")
    @Size(min = 7, max = 7, message = "O campo 'placa' deve ter 7 caracteres")
    private String placa;

    @NotBlank(message = "O campo 'modelo' é obrigatório")
    private String modelo;

    @NotBlank(message = "O campo 'status' é obrigatório")
    private String status;

    @OneToOne(cascade = CascadeType.ALL) 
    @JoinColumn(name = "qrcode_id") 
    private Qrcode qrcode;

    @ManyToOne
    @JoinColumn(name = "setor_id_setor")
    private Setor setor;

    @OneToMany(mappedBy = "moto")
    private List<Alerta> alertas;
}
