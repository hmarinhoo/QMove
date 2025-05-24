package br.com.fiap.qmove.specification;

import br.com.fiap.qmove.model.Alerta;
import br.com.fiap.qmove.model.enums.Tipo; 
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlertaSpecification {

    public static Specification<Alerta> filtro(
        String tipo,
        Boolean lido,
        LocalDateTime dataInicio,
        LocalDateTime dataFim
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tipo != null && !tipo.isEmpty()) {
                predicates.add(cb.equal(root.get("tipo"), Tipo.valueOf(tipo)));
            }
            if (lido != null) {
                predicates.add(cb.equal(root.get("lido"), lido));
            }
            if (dataInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataHora"), dataInicio));
            }
            if (dataFim != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataHora"), dataFim));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
