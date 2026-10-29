# E-Commerce Gateway & Dashboard - Composite API

Práctica individual de Desarrollo Web Integral (Unidad 3). API Compuesta (Aggregator) en **Java 21 + Spring Boot 3.2.5** que consulta en paralelo dos microservicios propios (catálogo e inventario) y una API externa de divisas (Frankfurter), y devuelve un único JSON consolidado.

## Tecnologías
Java 21, Spring Boot 3.2.5, Spring Data JPA, PostgreSQL, Maven, RestClient, CompletableFuture, Postman.

## Arquitectura

| Servicio | Tipo | Endpoint |
|---|---|---|
| Catálogo de productos | Propia | `GET /microservices/products/{id}` |
| Inventario y almacén | Propia | `GET /microservices/inventory/{id}` |
| Tipo de cambio | Terceros (Frankfurter) | `GET https://api.frankfurter.dev/v1/latest?base=USD&symbols=MXN` |
| **Composite API** | Propia | `GET /api/v1/product-summary/{id}` (alias: `/api/v1/products/{id}/summary`) |

El composite lanza las 3 consultas en paralelo con `CompletableFuture`, espera a que terminen y unifica las respuestas.

## Ejecución

### Prerrequisitos
- Java 21
- PostgreSQL con una base de datos llamada `ecommerce_db`

### Pasos
1. Clonar el repositorio y moverse a la rama `develop`.
2. Copiar `src/main/resources/application.properties-dev` a `application.properties` y poner tus credenciales de PostgreSQL.
3. Ejecutar:
   ```bash
   ./mvnw spring-boot:run
   ```
   (en Windows: `.\mvnw spring-boot:run`). Al iniciar se crean las tablas y se cargan datos de prueba desde `data.sql` (productos `P-101` y `P-102`).

## Probar los endpoints
```bash
curl http://localhost:8080/microservices/products/P-101
curl http://localhost:8080/microservices/inventory/P-101
curl http://localhost:8080/api/v1/product-summary/P-101
```

Ejemplo de respuesta del composite (el valor de `price_mxn` depende del tipo de cambio del día):
```json
{
  "product_id": "P-101",
  "title": "Laptop Gamer XYZ",
  "price_usd": 1200.0,
  "price_mxn": 20400.0,
  "stock_status": "IN_STOCK",
  "available_units": 15,
  "warehouse_location": "Almacen Chihuahua - Pasillo B3",
  "estimated_delivery_days": 3,
  "currency_conversion_source": "Frankfurter External API",
  "status": "SUCCESS"
}
```

## Manejo de errores y resiliencia

| Situación | Respuesta |
|---|---|
| Producto o inventario no existe | `404` con `{"status":"ERROR","message":"..."}` |
| Microservicio propio caído o con timeout | `503` con mensaje controlado |
| API de divisas caída o con timeout | `200` con `status: "PARTIAL"`, `price_mxn: null` y `currency_conversion_source: "UNAVAILABLE"` |

Se configuran timeouts de conexión (2 s) y lectura (3 s) en `application.properties`.

## Flujo de ramas (Feature Branch)
- `develop`: integración
- `feature/microservicio-productos`, `feature/inventario`, `feature/composite-api`: una rama por funcionalidad, integradas a `develop` mediante Pull Request.