package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Qrcode;
import br.com.fiap.qmove.repository.QrcodeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qrcode")
public class QrcodeController {

    @Autowired
    private QrcodeRepository repository;

    @GetMapping
    public List<Qrcode> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Qrcode> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Qrcode cadastrar(@RequestBody @Valid Qrcode qrcode) {
        return repository.save(qrcode);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Qrcode> atualizar(@PathVariable Long id, @RequestBody @Valid Qrcode qrcode) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        qrcode.setId(id);
        return ResponseEntity.ok(repository.save(qrcode));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
