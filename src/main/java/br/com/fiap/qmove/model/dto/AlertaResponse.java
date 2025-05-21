package br.com.fiap.qmove.model.dto;

import java.time.LocalDateTime;

public record AlertaResponse(
    int id,
    String tipo,
    String descricao,
    LocalDateTime dataHora,
    boolean lido,
    SetorResponse setor,
    MotoResponse moto
) {}
