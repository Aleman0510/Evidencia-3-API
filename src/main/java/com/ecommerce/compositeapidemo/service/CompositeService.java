package com.ecommerce.compositeapidemo.service;

import com.ecommerce.compositeapidemo.exception.ServiceException;
import com.ecommerce.compositeapidemo.model.CompositeProductResponse;
import com.ecommerce.compositeapidemo.model.ExchangeRateResponse;
import com.ecommerce.compositeapidemo.model.Inventory;
import com.ecommerce.compositeapidemo.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

@Service
public class CompositeService {

    private static final Logger log = LoggerFactory.getLogger(CompositeService.class);
    private static final String EXCHANGE_SOURCE = "Frankfurter External API";

    private final RestClient microservicesClient;
    private final RestClient exchangeClient;
    private final ExecutorService ioExecutor;

    public CompositeService(@Qualifier("microservicesClient") RestClient microservicesClient,
                            @Qualifier("exchangeClient") RestClient exchangeClient,
                            ExecutorService ioExecutor) {
        this.microservicesClient = microservicesClient;
        this.exchangeClient = exchangeClient;
        this.ioExecutor = ioExecutor;
    }

    public CompositeProductResponse getProductSummary(String id) {
        // 1. Lanzar las 3 peticiones en paralelo
        CompletableFuture<Product> productFuture = CompletableFuture
                .supplyAsync(() -> callMicroservice("/microservices/products/{id}", id, Product.class, "Catalogo"), ioExecutor);

        CompletableFuture<Inventory> inventoryFuture = CompletableFuture
                .supplyAsync(() -> callMicroservice("/microservices/inventory/{id}", id, Inventory.class, "Inventario"), ioExecutor);

        // La API externa NO es critica: si falla, se devuelve null y se responde en modo PARTIAL
        CompletableFuture<Double> rateFuture = CompletableFuture
                .supplyAsync(this::fetchUsdToMxnRate, ioExecutor)
                .exceptionally(ex -> {
                    log.warn("API de divisas no disponible, se responde sin precio MXN: {}", ex.getMessage());
                    return null;
                });

        // 2. Esperar a que terminen las 3
        try {
            CompletableFuture.allOf(productFuture, inventoryFuture, rateFuture).join();
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ServiceException serviceException) {
                throw serviceException; // 404 o 503 de un microservicio propio
            }
            log.error("Error inesperado en la orquestacion", e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error inesperado al consolidar la informacion del producto");
        }

        // 3. Consolidar en un unico contrato JSON
        Product product = productFuture.join();
        Inventory inventory = inventoryFuture.join();
        Double rate = rateFuture.join();

        Double priceMxn = null;
        if (rate != null && product.getPrice_usd() != null) {
            priceMxn = Math.round(product.getPrice_usd() * rate * 100.0) / 100.0;
        }

        boolean conversionOk = priceMxn != null;

        return new CompositeProductResponse(
                product.getProduct_id(),
                product.getTitle(),
                product.getPrice_usd(),
                priceMxn,
                inventory.getStock_status(),
                inventory.getAvailable_units(),
                inventory.getWarehouse_location(),
                inventory.getEstimated_delivery_days(),
                conversionOk ? EXCHANGE_SOURCE : "UNAVAILABLE",
                conversionOk ? "SUCCESS" : "PARTIAL"
        );
    }

    /** Consume un microservicio propio. 404 -> NOT_FOUND, cualquier falla de red/servidor -> 503. */
    private <T> T callMicroservice(String path, String id, Class<T> type, String serviceName) {
        try {
            T body = microservicesClient.get()
                    .uri(path, id)
                    .retrieve()
                    .body(type);
            if (body == null) {
                throw new ServiceException(HttpStatus.NOT_FOUND,
                        serviceName + ": no hay datos para el ID " + id);
            }
            return body;
        } catch (HttpClientErrorException.NotFound e) {
            throw new ServiceException(HttpStatus.NOT_FOUND,
                    serviceName + ": no se encontro el ID " + id);
        } catch (RestClientException e) {
            log.error("Fallo al consultar el microservicio de {}: {}", serviceName, e.getMessage());
            throw new ServiceException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Microservicio de " + serviceName + " no disponible");
        }
    }

    /** Consume la API externa Frankfurter y extrae la tasa USD -> MXN. */
    private Double fetchUsdToMxnRate() {
        ExchangeRateResponse response = exchangeClient.get()
                .uri("/v1/latest?base=USD&symbols=MXN")
                .retrieve()
                .body(ExchangeRateResponse.class);

        if (response == null || response.rates() == null || response.rates().get("MXN") == null) {
            throw new IllegalStateException("Respuesta invalida de la API de divisas");
        }
        return response.rates().get("MXN");
    }
}