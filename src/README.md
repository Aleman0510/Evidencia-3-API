# E-Commerce Gateway & Dashboard - Composite API

Proyecto desarrollado en **Java 21** y **Spring Boot** que implementa una arquitectura de microservicios y una **API Compuesta** para unificar la información de productos e inventarios en un panel de comercio electrónico.

---

## 🛠️ Tecnologías Utilizadas
* **Java 21**
* **Spring Boot 3.2.5**
* **Spring Data JPA**
* **PostgreSQL** (Base de datos relacional)
* **Maven** (Gestor de dependencias y construcción)
* **Postman** (Pruebas de endpoints)

---

## 📂 Arquitectura del Proyecto
El sistema se compone de los siguientes módulos y endpoints principales:
1. **Microservicio de Productos:** Gestión y consulta de detalles de productos.
2. **Microservicio de Inventario:** Control de stock, estatus y ubicación en almacén.
3. **API Compuesta:** Unifica ambos mundos en una sola respuesta JSON optimizada para dashboards.

---

## 🚀 Instrucciones de Ejecución

### Prerrequisitos
* Tener instalado **Java 21**.
* Tener instalado **Maven**.
* Tener un servidor de **PostgreSQL** corriendo con una base de datos creada llamada `ecommerce_db`.

### 1. Configuración de Base de Datos
Asegúrate de tener configuradas tus credenciales en el archivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true