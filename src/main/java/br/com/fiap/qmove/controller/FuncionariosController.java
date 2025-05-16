package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Funcionarios;
import br.com.fiap.qmove.repository.FuncionariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/funcionarios")
public class FuncionariosController {

    @Autowired
    private FuncionariosRepository repository;

    @GetMapping
    public List<Funcionarios> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionarios> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Funcionarios cadastrar(@RequestBody Funcionarios funcionario) {
        return repository.save(funcionario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionarios> atualizar(@PathVariable Long id, @RequestBody Funcionarios funcionario) {
        Optional<Funcionarios> existente = repository.findById(id);
        if (existente.isEmpty()) return ResponseEntity.notFound().build();

        funcionario.setId(id);
        return ResponseEntity.ok(repository.save(funcionario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


