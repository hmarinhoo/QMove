// QrcodeResponse.java
package br.com.fiap.qmove.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QrcodeResponse {
    private Long id;
    private String valor;
    private String tipo;
}
