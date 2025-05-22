package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Qrcode;
import br.com.fiap.qmove.repository.QrcodeRepository;
import br.com.fiap.specification.QrcodeSpecification;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qrcode")
@Slf4j
public class QrcodeController {

    @Autowired
    private QrcodeRepository repository;

    // Listar com paginação e cache
    @GetMapping
    @Cacheable(value = "qrcodes")
    public Page<Qrcode> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Qrcode> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar novo QrCode
    @PostMapping
    @CacheEvict(value = "qrcodes", allEntries = true)
    public Qrcode cadastrar(@RequestBody @Valid Qrcode qrcode) {
        return repository.save(qrcode);
    }

    // Atualizar QrCode
    @PutMapping("/{id}")
    @CacheEvict(value = "qrcodes", allEntries = true)
    public ResponseEntity<Qrcode> atualizar(@PathVariable Long id, @RequestBody @Valid Qrcode qrcode) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        qrcode.setId(id);
        return ResponseEntity.ok(repository.save(qrcode));
    }

    // Deletar QrCode
    @DeleteMapping("/{id}")
    @CacheEvict(value = "qrcodes", allEntries = true)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Filtro apenas por valor
    @GetMapping("/filtro")
    public List<Qrcode> filtrarPorValor(@RequestParam(required = false) String valor) {
        log.info("Filtrando QRCodes por valor='{}'", valor);

        Specification<Qrcode> spec = QrcodeSpecification.comValor(valor);
        return repository.findAll(spec);
    }
}
