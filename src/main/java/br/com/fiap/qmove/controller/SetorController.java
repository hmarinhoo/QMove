package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Setor;
import br.com.fiap.qmove.model.dto.SetorResponse;
import br.com.fiap.qmove.repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setor")
public class SetorController {

    @Autowired
    private SetorRepository repository;

    @GetMapping
    public List<SetorResponse> listar() {
        return repository.findAll()
                .stream()
                .map(setor -> new SetorResponse(setor.getId(), setor.getNome(), setor.getCodigo()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SetorResponse> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(setor -> new SetorResponse(setor.getId(), setor.getNome(), setor.getCodigo()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SetorResponse> cadastrar(@RequestBody Setor setor) {
        Setor salvo = repository.save(setor);
        return ResponseEntity.ok(new SetorResponse(salvo.getId(), salvo.getNome(), salvo.getCodigo()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SetorResponse> atualizar(@PathVariable Long id, @RequestBody Setor setor) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        setor.setId(id);
        Setor atualizado = repository.save(setor);
        return ResponseEntity.ok(new SetorResponse(atualizado.getId(), atualizado.getNome(), atualizado.getCodigo()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
