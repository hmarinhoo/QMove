package br.com.fiap.qmove.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.qmove.model.Funcionario;
import br.com.fiap.qmove.model.dto.FuncionarioResponse;
import br.com.fiap.qmove.repository.FuncionarioRepository;
import br.com.fiap.specification.FuncionarioSpecification;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public record FuncionarioFilter(String nome, String email) {}

    @Autowired
    private FuncionarioRepository repository;

    @GetMapping
    @Cacheable("funcionarios")
    public List<FuncionarioResponse> index(FuncionarioFilter filters) {
        log.info("Listando funcionários com filtros: nome={}, email={}", filters.nome(), filters.email());
        
        var funcionarios = repository.findAll(FuncionarioSpecification.filtro(filters.nome(), filters.email()));
        
        return funcionarios.stream()
            .map(func -> new FuncionarioResponse(func.getId(), func.getNome(), func.getEmail()))
            .toList();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @CacheEvict(value = "funcionarios", allEntries = true)
    public FuncionarioResponse create(@RequestBody @Valid Funcionario funcionario) {
        log.info("Cadastrando funcionário " + funcionario.getNome());

        Funcionario funcionarioSaved = repository.save(funcionario);

        return new FuncionarioResponse(
            funcionarioSaved.getId(),
            funcionarioSaved.getNome(),
            funcionarioSaved.getEmail()
        );
    }

    @GetMapping("{id}")
    public FuncionarioResponse get(@PathVariable Long id) {
        log.info("Buscando funcionário " + id);
        Funcionario funcionario = getFuncionario(id);
        return new FuncionarioResponse(
            funcionario.getId(),
            funcionario.getNome(),
            funcionario.getEmail()
        );
    }

    @DeleteMapping("{id}")
    @CacheEvict(value = "funcionarios", allEntries = true)
    public ResponseEntity<String> destroy(@PathVariable Long id) {
        log.info("Apagando funcionário " + id);
        Funcionario funcionario = getFuncionario(id);
        repository.delete(funcionario);
        log.info("Funcionário " + id + " apagado com sucesso");
        return ResponseEntity.ok("Funcionário com ID " + id + " foi deletado com sucesso.");
    }

    @PutMapping("{id}")
    @CacheEvict(value = "funcionarios", allEntries = true)
    public FuncionarioResponse update(@PathVariable Long id, @RequestBody @Valid Funcionario funcionario) {
        log.info("Atualizando funcionário " + id + " para " + funcionario);

        Funcionario existingFuncionario = getFuncionario(id);

        existingFuncionario.setNome(funcionario.getNome());
        existingFuncionario.setEmail(funcionario.getEmail());
        existingFuncionario.setSenha(funcionario.getSenha());

        Funcionario updatedFuncionario = repository.save(existingFuncionario);

        return new FuncionarioResponse(
            updatedFuncionario.getId(),
            updatedFuncionario.getNome(),
            updatedFuncionario.getEmail()
        );
    }

    private Funcionario getFuncionario(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
    }
}
