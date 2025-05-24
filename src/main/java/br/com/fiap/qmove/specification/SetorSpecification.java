package br.com.fiap.qmove.specification;

import br.com.fiap.qmove.model.Setor;
import org.springframework.data.jpa.domain.Specification;

public class SetorSpecification {

  public static Specification<Setor> comNome(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }

    public static Specification<Setor> comCodigo(String codigo) {
        return (root, query, cb) -> {
            if (codigo == null || codigo.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("codigo")), "%" + codigo.toLowerCase() + "%");
        };
    }
}
