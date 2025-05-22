package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Setor;
import br.com.fiap.qmove.model.dto.SetorResponse;
import br.com.fiap.qmove.repository.SetorRepository;
import br.com.fiap.specification.SetorSpecification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/setor")
public class SetorController {

    @Autowired
    private SetorRepository repository;

    // Listar com paginação + cache
    @GetMapping
    @Cacheable(value = "setores")
    public Page<SetorResponse> listar(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String codigo,
        Pageable pageable
    ) {
        Specification<Setor> spec = Specification.where(null);
        if (nome != null && !nome.isEmpty()) {
            spec = spec.and(SetorSpecification.comNome(nome));
        }
        if (codigo != null && !codigo.isEmpty()) {
            spec = spec.and(SetorSpecification.comCodigo(codigo));
        }
        return repository.findAll(spec, pageable)
                .map(this::toSetorResponse);
}

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<SetorResponse> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(this::toSetorResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cadastrar novo setor
    @PostMapping
    @CacheEvict(value = "setores", allEntries = true)
    public ResponseEntity<SetorResponse> cadastrar(@RequestBody @Valid Setor setor) {
        Setor salvo = repository.save(setor);
        return ResponseEntity.ok(toSetorResponse(salvo));
    }

    // Atualizar setor
    @PutMapping("/{id}")
    @CacheEvict(value = "setores", allEntries = true)
    public ResponseEntity<SetorResponse> atualizar(@PathVariable Long id, @RequestBody @Valid Setor setor) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        setor.setId(id);
        Setor atualizado = repository.save(setor);
        return ResponseEntity.ok(toSetorResponse(atualizado));
    }

    // Deletar setor
    @DeleteMapping("/{id}")
    @CacheEvict(value = "setores", allEntries = true)
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        var setor = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Setor não encontrado"));
        repository.delete(setor);
        return ResponseEntity.ok("Setor com ID " + id + " foi deletado com sucesso.");
}

    // Filtro apenas por nome
    @GetMapping("/filtro")
    public List<Setor> filtrarPorNome(@RequestParam(required = false) String nome) {
        Specification<Setor> spec = SetorSpecification.comNome(nome);
        return repository.findAll(spec);
    }

    // Conversão de entidade para DTO
    private SetorResponse toSetorResponse(Setor setor) {
        return new SetorResponse(
                setor.getId(),
                setor.getNome(),
                setor.getCodigo()
        );
    }
}
