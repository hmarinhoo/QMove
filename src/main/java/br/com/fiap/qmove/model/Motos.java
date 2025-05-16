package br.com.fiap.qmove.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Motos {
    @Id
    private Long id;
    private String placa;
    private String modelo;
    private String status;

    @OneToOne(cascade = CascadeType.ALL) // Para ocorrer a persistência do qrcode junto com a moto
    @JoinColumn(name = "qrcode_id") // Criando a chave estrangeira
    private Qrcode qrcode;
}
