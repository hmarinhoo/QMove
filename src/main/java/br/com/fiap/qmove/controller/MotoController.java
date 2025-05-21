package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Moto;
import br.com.fiap.qmove.model.Qrcode;
import br.com.fiap.qmove.model.dto.MotoResponse;
import br.com.fiap.qmove.repository.MotoRepository;
import br.com.fiap.qmove.repository.QrcodeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/moto")
public class MotoController {

    @Autowired
    private MotoRepository repository;

    @Autowired
    private QrcodeRepository qrCodeRepository;

    @GetMapping
    public List<MotoResponse> listar() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotoResponse> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MotoResponse> cadastrar(@RequestBody MotoResponse dto) {
        Moto moto = toEntity(dto);
        Moto salva = repository.save(moto);
        return ResponseEntity.ok(toDto(salva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoResponse> atualizar(@PathVariable Long id, @RequestBody MotoResponse dto) {
        Optional<Moto> motoOptional = repository.findById(id);
        if (motoOptional.isEmpty()) return ResponseEntity.notFound().build();

        Moto moto = motoOptional.get();
        moto.setStatus(dto.getStatus());
        moto.setModelo(dto.getModelo());
        moto.setPlaca(dto.getPlaca());

        if (dto.getQrcodeId() != null) {
            Optional<Qrcode> qrcodeOptional = qrCodeRepository.findById(dto.getQrcodeId());
            if (qrcodeOptional.isPresent()) {
                moto.setQrcode(qrcodeOptional.get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            moto.setQrcode(null);
        }

        return ResponseEntity.ok(toDto(repository.save(moto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Conversão
    private MotoResponse toDto(Moto moto) {
        MotoResponse dto = new MotoResponse();
        dto.setId(moto.getId());
        dto.setStatus(moto.getStatus());
        dto.setModelo(moto.getModelo());
        dto.setPlaca(moto.getPlaca());
        if (moto.getQrcode() != null) {
            dto.setQrcodeId(moto.getQrcode().getId());
        }
        return dto;
    }

    private Moto toEntity(MotoResponse dto) {
        Moto moto = new Moto();
        moto.setId(dto.getId());
        moto.setStatus(dto.getStatus());
        moto.setModelo(dto.getModelo());
        moto.setPlaca(dto.getPlaca());

        if (dto.getQrcodeId() != null) {
            Optional<Qrcode> qrOpt = qrCodeRepository.findById(dto.getQrcodeId());
            qrOpt.ifPresent(moto::setQrcode);
        }

        return moto;
    }
}
