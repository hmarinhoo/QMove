package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Alertas;
import br.com.fiap.qmove.repository.AlertasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alertas")
public class AlertasController {

    @Autowired
    private AlertasRepository repository;

    @GetMapping
    public List<Alertas> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alertas> buscarPorId(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Alertas cadastrar(@RequestBody Alertas alerta) {
        return repository.save(alerta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alertas> atualizar(@PathVariable int id, @RequestBody Alertas alerta) {
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
