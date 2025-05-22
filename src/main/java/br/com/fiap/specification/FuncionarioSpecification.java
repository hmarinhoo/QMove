package br.com.fiap.specification;


import br.com.fiap.qmove.model.Funcionario;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class FuncionarioSpecification {

    public static Specification<Funcionario> filtro(String nome, String email) {
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (nome != null && !nome.isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (email != null && !email.isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            return p;
        };
    }
}

