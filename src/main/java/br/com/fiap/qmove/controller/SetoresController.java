package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Setores;
import br.com.fiap.qmove.repository.SetoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setores")
public class SetoresController {

    @Autowired
    private SetoresRepository repository;

    @GetMapping
    public List<Setores> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Setores> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Setores cadastrar(@RequestBody Setores setor) {
        return repository.save(setor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Setores> atualizar(@PathVariable Long id, @RequestBody Setores setor) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        setor.setId(id);
        return ResponseEntity.ok(repository.save(setor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
