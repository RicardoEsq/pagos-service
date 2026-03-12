package com.resquivel.parcial.pagos_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.resquivel.parcial.pagos_service.model.Pago;
import com.resquivel.parcial.pagos_service.repository.PagoRepository;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private CloudWatchService cloudWatchService;

    // POST /pagos/procesar - Procesar un nuevo pago
    @PostMapping("/procesar")
    public Pago procesarPago(@RequestBody Pago pago) {
        logger.info("Procesando pago para la orden: {}", pago.getOrdenId());
        pago.setEstado("PROCESADO");
        cloudWatchService.enviarLog("Pago procesado exitosamente: Orden " + pago.getOrdenId());
        return pagoRepository.save(pago);
    }

    // GET /pagos/{id} - Consultar detalle de pago por ID de pago
    @GetMapping("/{id}")
    public org.springframework.http.ResponseEntity<Pago> consultarPago(@PathVariable("id") String id) {
        logger.info("Consultando pago ID: {}", id);
        return pagoRepository.findById(id)
                .map(pago -> org.springframework.http.ResponseEntity.ok(pago))
                .orElse(org.springframework.http.ResponseEntity.notFound().build());
    }

    // GET /pagos/orden/{ordenId} - Consultar pago por ID de orden
    @GetMapping("/orden/{ordenId}")
    public org.springframework.http.ResponseEntity<Pago> consultarPagoPorOrden(@PathVariable("ordenId") String ordenId) {
        logger.info("Buscando pago para la orden ID: {}", ordenId);
        return pagoRepository.findByOrdenId(ordenId)
                .map(pago -> org.springframework.http.ResponseEntity.ok(pago))
                .orElse(org.springframework.http.ResponseEntity.notFound().build());
    }

    // PUT /pagos/{id}/reembolso - Procesar reembolso de un pago
    @PutMapping("/{id}/reembolso")
    public org.springframework.http.ResponseEntity<Pago> procesarReembolso(@PathVariable("id") String id) {
        logger.info("Procesando reembolso para el pago ID: {}", id);
        return pagoRepository.findById(id).map(pago -> {
            pago.setEstado("REEMBOLSADO");
            try {
                cloudWatchService.enviarLog("Reembolso procesado para el pago: " + id);
            } catch (Exception e) {}
            return org.springframework.http.ResponseEntity.ok(pagoRepository.save(pago));
        }).orElse(org.springframework.http.ResponseEntity.notFound().build());
    }
}