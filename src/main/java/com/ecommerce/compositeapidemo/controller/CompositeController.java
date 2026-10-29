package com.ecommerce.compositeapidemo.controller;

import com.ecommerce.compositeapidemo.model.CompositeProductResponse;
import com.ecommerce.compositeapidemo.model.Inventory;
import com.ecommerce.compositeapidemo.model.Product;
import com.ecommerce.compositeapidemo.repository.InventoryRepository;
import com.ecommerce.compositeapidemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/microservices")
public class CompositeController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping("/composite/products/{id}")
    public ResponseEntity<?> getCompositeProduct(@PathVariable String id) {
        // Bloque try/catch para manejo de tolerancia a fallos y resiliencia
        try {
            // Consultas independientes a los repositorios simulando microservicios
            Optional<Product> productOpt = productRepository.findById(id);
            Optional<Inventory> inventoryOpt = inventoryRepository.findById(id);

            // Validación de existencia en ambos servicios
            if (productOpt.isPresent() && inventoryOpt.isPresent()) {
                Product p = productOpt.get();
                Inventory i = inventoryOpt.get();

                CompositeProductResponse response = new CompositeProductResponse(
                    p.getProduct_id(),
                    p.getTitle(),
                    p.getPrice_usd(),
                    i.getAvailable_units(),
                    i.getStock_status(),
                    i.getWarehouse_location(),
                    i.getEstimated_delivery_days()
                );

                return ResponseEntity.ok(response);
            } else {
                // Control lógico si el recurso no es encontrado en alguno de los servicios
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\": \"Producto o inventario no encontrado para el ID: " + id + "\"}");
            }

        } catch (Exception e) {
            // Captura de excepciones críticas (ej. caída de la base de datos o fallo de red)
            // Esto garantiza resiliencia evitando que el servidor colapse y devolviendo un mensaje controlado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Error de tolerancia a fallos: No se pudo completar la operación compuesta\", \"detalle\": \"" + e.getMessage() + "\"}");
        }
    }
}