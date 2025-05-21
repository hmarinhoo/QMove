package br.com.fiap.qmove.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Qrcode {
    @Id
    private Long id;
    private String valor;
    private String tipo;

    @OneToOne(mappedBy = "qrcode")
    @JsonIgnore // evita loop de serialização
    private Moto moto;
}
