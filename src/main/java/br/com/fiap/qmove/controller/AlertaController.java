package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Alerta;
import br.com.fiap.qmove.repository.AlertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerta")
public class AlertaController {

    @Autowired
    private AlertaRepository repository;

    @GetMapping
    public List<Alerta> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alerta> buscarPorId(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Alerta cadastrar(@RequestBody Alerta alerta) {
        return repository.save(alerta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alerta> atualizar(@PathVariable int id, @RequestBody Alerta alerta) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alerta.setId(id); // garante que estamos atualizando o certo
        return ResponseEntity.ok(repository.save(alerta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
