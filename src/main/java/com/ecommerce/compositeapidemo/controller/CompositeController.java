package com.ecommerce.compositeapidemo.controller;

import com.ecommerce.compositeapidemo.model.CompositeProductResponse;
import com.ecommerce.compositeapidemo.service.CompositeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CompositeController {

    private final CompositeService compositeService;

    public CompositeController(CompositeService compositeService) {
        this.compositeService = compositeService;
    }

    @GetMapping({"/product-summary/{id}", "/products/{id}/summary"})
    public ResponseEntity<CompositeProductResponse> getProductSummary(@PathVariable String id) {
        return ResponseEntity.ok(compositeService.getProductSummary(id));
    }
}