package com.resquivel.parcial.pagos_service.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.resquivel.parcial.pagos_service.model.Pago;

@Repository
public interface PagoRepository extends MongoRepository<Pago, String> {
    Optional<Pago> findByOrdenId(String ordenId);
}