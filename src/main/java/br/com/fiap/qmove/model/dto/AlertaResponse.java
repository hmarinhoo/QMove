package br.com.fiap.qmove.model.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AlertaResponse {
    private Long id;
    private String tipo;
    private String descricao;
    private LocalDateTime dataHora;
    private boolean lido;
    private Long motoId;
    private Long setorId;

    public AlertaResponse(Long id, String tipo, String descricao, LocalDateTime dataHora, boolean lido, Long motoId, Long setorId) {
        this.id = id;
        this.tipo = tipo;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.lido = lido;
        this.motoId = motoId;
        this.setorId = setorId;
    }
}