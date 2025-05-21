package br.com.fiap.qmove.controller;

import br.com.fiap.qmove.model.Alerta;
import br.com.fiap.qmove.model.Moto;
import br.com.fiap.qmove.model.Qrcode;
import br.com.fiap.qmove.model.Setor;
import br.com.fiap.qmove.model.dto.MotoResponse;
import br.com.fiap.qmove.model.dto.QrcodeResponse;
import br.com.fiap.qmove.model.dto.SetorResponse;
import br.com.fiap.qmove.model.enums.Tipo;
import br.com.fiap.qmove.repository.AlertaRepository;
import br.com.fiap.qmove.repository.MotoRepository;
import br.com.fiap.qmove.repository.QrcodeRepository;
import br.com.fiap.qmove.repository.SetorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/moto")
public class MotoController {

    @Autowired
    private MotoRepository repository;

    @Autowired
    private QrcodeRepository qrCodeRepository;

    @Autowired
    private SetorRepository setorRepository;

    @Autowired
    private AlertaRepository alertaRepository;

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
        if (motoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Moto moto = motoOptional.get();

        // Guarda o setor antigo para comparação
        Setor setorAntigo = moto.getSetor();

        // Atualiza campos básicos
        moto.setStatus(dto.getStatus());
        moto.setModelo(dto.getModelo());
        moto.setPlaca(dto.getPlaca());

        // Atualiza Qrcode
        if (dto.getQrcode() != null) {
            Optional<Qrcode> qrcodeOptional = qrCodeRepository.findById(dto.getQrcode().getId());
            if (qrcodeOptional.isPresent()) {
                moto.setQrcode(qrcodeOptional.get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            moto.setQrcode(null);
        }

        // Atualiza setor
        Setor novoSetor = null;
        if (dto.getSetor() != null) {
            Optional<Setor> setorOptional = setorRepository.findById(dto.getSetor().getId());
            if (setorOptional.isPresent()) {
                novoSetor = setorOptional.get();
                moto.setSetor(novoSetor);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            moto.setSetor(null);
        }

        // Salva moto atualizada
        Moto atualizado = repository.save(moto);

        // Cria alerta se setor mudou
        if ((setorAntigo == null && novoSetor != null) ||
            (setorAntigo != null && (novoSetor == null || !setorAntigo.getId().equals(novoSetor.getId())))) {

            Alerta alerta = new Alerta();
            alerta.setTipo(Tipo.MOVIMENTACAO.name());
            alerta.setMoto(atualizado);
            alerta.setSetor(novoSetor);
            alerta.setLido(false);
            alerta.setDataHora(LocalDateTime.now());

            String descAntigo = setorAntigo != null ? setorAntigo.getNome() : "Nenhum setor";
            String descNovo = novoSetor != null ? novoSetor.getNome() : "Nenhum setor";
            alerta.setDescricao("Moto saiu do setor '" + descAntigo + "' e foi para o setor '" + descNovo + "'.");

            alertaRepository.save(alerta);
        }

        return ResponseEntity.ok(toDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private MotoResponse toDto(Moto moto) {
        QrcodeResponse qrcodeDto = null;
        if (moto.getQrcode() != null) {
            qrcodeDto = toQrcodeDto(moto.getQrcode());
        }

        SetorResponse setorDto = null;
        if (moto.getSetor() != null) {
            setorDto = toSetorDto(moto.getSetor());
        }

        return new MotoResponse(
                moto.getId(),
                moto.getStatus(),
                moto.getModelo(),
                moto.getPlaca(),
                qrcodeDto,
                setorDto
        );
    }

    private QrcodeResponse toQrcodeDto(Qrcode qrcode) {
        return new QrcodeResponse(
                qrcode.getId(),
                qrcode.getValor(),
                qrcode.getTipo()
        );
    }

    private SetorResponse toSetorDto(Setor setor) {
        return new SetorResponse(
                setor.getId(),
                setor.getNome(),
                setor.getCodigo()
        );
    }

    private Moto toEntity(MotoResponse dto) {
        Moto moto = new Moto();
        moto.setId(dto.getId());
        moto.setStatus(dto.getStatus());
        moto.setModelo(dto.getModelo());
        moto.setPlaca(dto.getPlaca());

        if (dto.getQrcode() != null) {
            qrCodeRepository.findById(dto.getQrcode().getId()).ifPresent(moto::setQrcode);
        } else {
            moto.setQrcode(null);
        }

        if (dto.getSetor() != null) {
            setorRepository.findById(dto.getSetor().getId()).ifPresent(moto::setSetor);
        } else {
            moto.setSetor(null);
        }

        return moto;
    }
}
