package br.com.fiap.qmove.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fiap.qmove.model.Moto;

public interface MotoRepository extends JpaRepository<Moto, Long>{
    
}
