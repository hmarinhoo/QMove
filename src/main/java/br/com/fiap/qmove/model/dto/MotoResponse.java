package br.com.fiap.qmove.model.dto;

import lombok.Data;

@Data
public class MotoResponse {
    private Long id;
    private String status;
    private String modelo;
    private String placa;
    private Long qrcodeId;
}
