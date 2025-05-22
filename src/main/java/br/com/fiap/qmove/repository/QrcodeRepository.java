package br.com.fiap.qmove.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fiap.qmove.model.Qrcode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QrcodeRepository extends JpaRepository<Qrcode, Long>, JpaSpecificationExecutor<Qrcode> {
}
