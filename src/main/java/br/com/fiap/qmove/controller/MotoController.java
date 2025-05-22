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
import br.com.fiap.specification.MotoSpecification;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/motos")
public class MotoController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // DTO para filtros
    public record MotoFilter(String modelo, String placa) {}

    @Autowired
    private MotoRepository repository;

    @Autowired
    private QrcodeRepository qrcodeRepository;

    @Autowired
    private SetorRepository setorRepository;

    @Autowired
    private AlertaRepository alertaRepository;

    @GetMapping
    @Cacheable("motos")
    public Page<MotoResponse> listar(MotoFilter filters, Pageable pageable) {
        log.info("Listando motos com filtros: modelo={}, placa={}", filters.modelo(), filters.placa());

        var motos = repository.findAll(MotoSpecification.withFilters(filters), pageable);

        return motos.map(this::toDto);
    }

    @GetMapping("/{id}")
    public MotoResponse buscarPorId(@PathVariable Long id) {
        log.info("Buscando moto por id: {}", id);
        Moto moto = getMoto(id);
        return toDto(moto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "motos", allEntries = true)
    public MotoResponse cadastrar(@RequestBody @Valid MotoResponse dto) {
        log.info("Cadastrando moto modelo: {}", dto.getModelo());
        Moto moto = toEntity(dto);

        // Validar e setar Qrcode
        if (dto.getQrcode() != null && dto.getQrcode().getId() != null) {
            var qrcode = qrcodeRepository.findById(dto.getQrcode().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "QR Code não encontrado"));
            moto.setQrcode(qrcode);
        }

        // Validar e setar Setor
        if (dto.getSetor() != null && dto.getSetor().getId() != null) {
            var setor = setorRepository.findById(dto.getSetor().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Setor não encontrado"));
            moto.setSetor(setor);
        }

        Moto salva = repository.save(moto);
        return toDto(salva);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "motos", allEntries = true)
    public MotoResponse atualizar(@PathVariable Long id, @RequestBody @Valid MotoResponse dto) {
        log.info("Atualizando moto id: {}", id);

        Moto moto = getMoto(id);
        Setor setorAntigo = moto.getSetor();

        moto.setStatus(dto.getStatus());
        moto.setModelo(dto.getModelo());
        moto.setPlaca(dto.getPlaca());

        if (dto.getQrcode() != null && dto.getQrcode().getId() != null) {
            var qrcode = qrcodeRepository.findById(dto.getQrcode().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "QR Code não encontrado"));
            moto.setQrcode(qrcode);
        } else {
            moto.setQrcode(null);
        }

        Setor novoSetor = null;
        if (dto.getSetor() != null && dto.getSetor().getId() != null) {
            novoSetor = setorRepository.findById(dto.getSetor().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Setor não encontrado"));
            moto.setSetor(novoSetor);
        } else {
            moto.setSetor(null);
        }

        Moto atualizado = repository.save(moto);

        // Criar alerta se setor mudou
        if ((setorAntigo == null && novoSetor != null)
                || (setorAntigo != null && (novoSetor == null || !setorAntigo.getId().equals(novoSetor.getId())))) {

            Alerta alerta = new Alerta();
            alerta.setTipo(Tipo.MOVIMENTACAO);
            alerta.setMoto(atualizado);
            alerta.setSetor(novoSetor);
            alerta.setLido(false);
            alerta.setDataHora(LocalDateTime.now());

            String descAntigo = setorAntigo != null ? setorAntigo.getNome() : "Nenhum setor";
            String descNovo = novoSetor != null ? novoSetor.getNome() : "Nenhum setor";
            alerta.setDescricao("Moto saiu do setor '" + descAntigo + "' e foi para o setor '" + descNovo + "'.");

            alertaRepository.save(alerta);
        }

        return toDto(atualizado);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "motos", allEntries = true)
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        log.info("Deletando moto id: {}", id);
        Moto moto = getMoto(id);
        repository.delete(moto);
        return ResponseEntity.ok("Moto com ID " + id + " deletada com sucesso.");
    }

    private Moto getMoto(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada"));
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

        if (dto.getQrcode() != null && dto.getQrcode().getId() != null) {
            qrcodeRepository.findById(dto.getQrcode().getId()).ifPresent(moto::setQrcode);
        } else {
            moto.setQrcode(null);
        }

        if (dto.getSetor() != null && dto.getSetor().getId() != null) {
            setorRepository.findById(dto.getSetor().getId()).ifPresent(moto::setSetor);
        } else {
            moto.setSetor(null);
        }

        return moto;
    }
}
