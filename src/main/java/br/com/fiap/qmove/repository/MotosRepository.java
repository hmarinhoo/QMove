package br.com.fiap.qmove.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fiap.qmove.model.Motos;

public interface MotosRepository extends JpaRepository<Motos, Long>{
    
}
