package br.com.fiap.qmove.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Qrcode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @NotBlank(message = "O campo 'valor' do QrCode é obrigatório")
    private String valor;

    @NotBlank(message = "O campo 'tipo' do QrCode é obrigatório")
    private String tipo; //Exemplo: ativo / inativo

    @OneToOne(mappedBy = "qrcode")
    @JsonIgnore 
    private Moto moto;
}
