package br.com.fiap.qmove.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Motos {

    @Id
    private Long id;
    private String placa;
    private String modelo;
    private Setor setores;
    private String qrCode; //para guardar o texto do QRCode
    private LocalDateTime dataCadastro;
}
