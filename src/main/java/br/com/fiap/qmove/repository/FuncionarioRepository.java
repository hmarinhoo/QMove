package br.com.fiap.qmove.repository;

import br.com.fiap.qmove.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {}
