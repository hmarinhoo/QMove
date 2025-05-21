package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Alerta;
import br.com.fiap.qmove.model.dto.AlertaResponse;
import br.com.fiap.qmove.repository.AlertaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alertas")
public class AlertasController {

    @Autowired
    private AlertaRepository alertaRepository;

    @GetMapping
    public List<AlertaResponse> listar() {
        return alertaRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponse> buscarPorId(@PathVariable int id) {
        return alertaRepository.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertaResponse> atualizar(@PathVariable int id, @RequestBody Alerta alerta) {
        return alertaRepository.findById(id)
                .map(existing -> {
                    existing.setLido(alerta.isLido()); // exemplo: só para marcar como lido
                    alertaRepository.save(existing);
                    return ResponseEntity.ok(toDto(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        if (!alertaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alertaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private AlertaResponse toDto(Alerta alerta) {
        Long motoId = alerta.getMoto() != null ? alerta.getMoto().getId() : null;
        Long setorId = alerta.getSetor() != null ? alerta.getSetor().getId() : null;
        return new AlertaResponse(
                alerta.getId(),
                alerta.getTipo(),
                alerta.getDescricao(),
                alerta.getDataHora(),
                alerta.isLido(),
                motoId,
                setorId
        );
    }
}
