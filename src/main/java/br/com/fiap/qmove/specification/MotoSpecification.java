package br.com.fiap.qmove.specification;

import br.com.fiap.qmove.controller.MotoController.MotoFilter;
import br.com.fiap.qmove.model.Moto;
import org.springframework.data.jpa.domain.Specification;

public class MotoSpecification {
    public static Specification<Moto> withFilters(MotoFilter filter) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (filter.modelo() != null && !filter.modelo().isEmpty()) {
                predicates = cb.and(predicates, cb.like(cb.lower(root.get("modelo")), "%" + filter.modelo().toLowerCase() + "%"));
            }
            if (filter.placa() != null && !filter.placa().isEmpty()) {
                predicates = cb.and(predicates, cb.like(cb.lower(root.get("placa")), "%" + filter.placa().toLowerCase() + "%"));
            }
            return predicates;
        };
    }
}