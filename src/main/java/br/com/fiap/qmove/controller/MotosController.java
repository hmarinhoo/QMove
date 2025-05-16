package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Motos;
import br.com.fiap.qmove.model.Qrcode;
import br.com.fiap.qmove.repository.MotosRepository;
import br.com.fiap.qmove.repository.QrcodeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/motos")
public class MotosController {

    @Autowired
    private MotosRepository repository;

    @Autowired
    private QrcodeRepository qrCodeRepository;

    @GetMapping
    public List<Motos> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Motos> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Motos cadastrar(@RequestBody Motos moto) {
        return repository.save(moto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Motos> atualizar(@PathVariable Long id, @RequestBody Motos motoAtualizada) {
        Optional<Motos> motoOptional = repository.findById(id);

        if (motoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Motos moto = motoOptional.get();

        // Atualiza os campos básicos
        moto.setStatus(motoAtualizada.getStatus());
        moto.setModelo(motoAtualizada.getModelo());
        moto.setPlaca(motoAtualizada.getPlaca());

        // Associação do QRCode
        if (motoAtualizada.getQrcode() != null) {
            Long qrcodeId = motoAtualizada.getQrcode().getId();
            Optional<Qrcode> qrcodeOptional = qrCodeRepository.findById(qrcodeId);
            if (qrcodeOptional.isPresent()) {
                moto.setQrcode(qrcodeOptional.get());
            } else {
                // QRCode não encontrado, pode retornar erro ou limpar a associação
                return ResponseEntity.badRequest().body(null);
            }
        } else {
            // Remove associação do QRCode, se necessário
            moto.setQrcode(null);
        }

        repository.save(moto);
        return ResponseEntity.ok(moto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
