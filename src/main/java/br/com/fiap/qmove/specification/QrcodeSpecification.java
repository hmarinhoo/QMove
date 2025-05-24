package br.com.fiap.qmove.specification;

import br.com.fiap.qmove.model.Qrcode;
import org.springframework.data.jpa.domain.Specification;

public class QrcodeSpecification {

    public static Specification<Qrcode> comValor(String valor) {
        return (root, query, builder) ->
            valor == null ? null : builder.like(builder.lower(root.get("valor")), "%" + valor.toLowerCase() + "%");
    }
}
