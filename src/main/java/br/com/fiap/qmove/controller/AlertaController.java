package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Alerta;
import br.com.fiap.qmove.model.dto.AlertaResponse;
import br.com.fiap.qmove.repository.AlertaRepository;
import br.com.fiap.specification.AlertaSpecification;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/alertas")
public class AlertaController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public record AlertaFilter(String tipo, Boolean lido, LocalDateTime dataInicio, LocalDateTime dataFim) {}

    @Autowired
    private AlertaRepository alertaRepository;

    @GetMapping
    @Cacheable("alertas")
    public List<AlertaResponse> listar(AlertaFilter filtro) {
        log.info("Listando alertas com filtros: tipo={}, lido={}, dataInicio={}, dataFim={}",
                filtro.tipo(), filtro.lido(), filtro.dataInicio(), filtro.dataFim());

        var alertas = alertaRepository.findAll(AlertaSpecification.filtro(
                filtro.tipo(), filtro.lido(), filtro.dataInicio(), filtro.dataFim()));

        return alertas.stream()
                .map(this::toDto)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "alertas", allEntries = true)
    public AlertaResponse criar(@RequestBody @Valid Alerta alerta) {
        log.info("Criando alerta do tipo {}", alerta.getTipo());

        var alertaSalvo = alertaRepository.save(alerta);
        return toDto(alertaSalvo);
    }

    @GetMapping("/{id}")
    public AlertaResponse buscarPorId(@PathVariable Long id) {
        log.info("Buscando alerta com id {}", id);
        Alerta alerta = getAlerta(id);
        return toDto(alerta);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "alertas", allEntries = true)
    public AlertaResponse atualizar(@PathVariable Long id, @RequestBody @Valid Alerta alerta) {
        log.info("Atualizando alerta id {}", id);

        Alerta existente = getAlerta(id);

        existente.setTipo(alerta.getTipo());
        existente.setDescricao(alerta.getDescricao());
        existente.setDataHora(alerta.getDataHora());
        existente.setLido(alerta.isLido());
        existente.setMoto(alerta.getMoto());
        existente.setSetor(alerta.getSetor());

        var atualizado = alertaRepository.save(existente);
        return toDto(atualizado);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "alertas", allEntries = true)
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        log.info("Deletando alerta id {}", id);
        Alerta alerta = getAlerta(id);
        alertaRepository.delete(alerta);
        return ResponseEntity.ok("Alerta com ID " + id + " deletado com sucesso.");
    }

    private Alerta getAlerta(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta não encontrado"));
    }

    private AlertaResponse toDto(Alerta alerta) {
        Long motoId = alerta.getMoto() != null ? alerta.getMoto().getId() : null;
        Long setorId = alerta.getSetor() != null ? alerta.getSetor().getId() : null;
        return new AlertaResponse(
                alerta.getId(),
                alerta.getTipo() != null ? alerta.getTipo().name() : null,
                alerta.getDescricao(),
                alerta.getDataHora(),
                alerta.isLido(),
                motoId,
                setorId
        );
    }
}
