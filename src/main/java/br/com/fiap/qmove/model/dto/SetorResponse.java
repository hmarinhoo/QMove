package br.com.fiap.qmove.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class SetorResponse {
    private Long id;
    private String nome;
    private String codigo;
}
