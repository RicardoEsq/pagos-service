package com.resquivel.parcial.pagos_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.resquivel.parcial.pagos_service.model.Pago; // <-- Ajusta tu paquete aquí

@Repository
public interface PagoRepository extends MongoRepository<Pago, String> {
}
