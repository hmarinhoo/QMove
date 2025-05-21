// MotoResponse.java (como já corrigido)
package br.com.fiap.qmove.model.dto;

import lombok.Data;

@Data
public class MotoResponse {
    private Long id;
    private String placa;
    private String modelo;
    private String status;
    private QrcodeResponse qrcode;
    private SetorResponse setor;

    public MotoResponse(Long id, String status, String modelo, String placa,
                        QrcodeResponse qrcode, SetorResponse setor) {
        this.id = id;
        this.status = status;
        this.modelo = modelo;
        this.placa = placa;
        this.qrcode = qrcode;
        this.setor = setor;
    }
}
