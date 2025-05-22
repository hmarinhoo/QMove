package br.com.fiap.specification;

import br.com.fiap.qmove.model.Setor;
import org.springframework.data.jpa.domain.Specification;

public class SetorSpecification {

    public static Specification<Setor> comNome(String nome) {
        return (root, query, builder) -> {
            if (nome == null) return null;
            return builder.like(builder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }
}
