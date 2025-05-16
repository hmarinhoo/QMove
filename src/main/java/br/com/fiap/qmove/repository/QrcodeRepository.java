package br.com.fiap.qmove.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fiap.qmove.model.Qrcode;

public interface QrcodeRepository extends JpaRepository<Qrcode, Long>{
    
}
