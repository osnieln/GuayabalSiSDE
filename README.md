# SGPA — API

API REST del **SGPA (Sistema de Gestión de la Producción Agrícola)**, una aplicación web para la granja universitaria "El Guayabal" de la Universidad Agraria de La Habana (UNAH). Centraliza las áreas de cultivo, la producción, el riego, los agroquímicos, las alertas y los reportes.

Proyecto de tesis de Ingeniería Informática, **en desarrollo activo**.

La interfaz web está en un repositorio aparte: [GuayabalSiSDE-Front](https://github.com/osnieln/GuayabalSiSDE-Front), que incluye capturas y el manual de usuario.

## Funcionalidades

- **Áreas** guardadas como polígonos geoespaciales, listas para mostrarse sobre un mapa.
- **Cultivos y tipos de cultivo**, con su asignación a cada área por períodos.
- **Producción** y **planificación de cosecha**, con estados de seguimiento.
- **Riego**: planificación e historial.
- **Agroquímicos**: inventario con movimientos de entrada y salida.
- **Alertas automáticas** generadas por tareas programadas.
- **Panel de indicadores** y rendimiento por área y cultivo.
- **Trabajadores**, **usuarios** y **roles**.
- **Reportes en PDF** y exportación a Excel.
- Documentación interactiva de la API con Swagger.

## Tecnologías

| Capa | Herramientas |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.2 (Web, Data JPA, tareas programadas) |
| Base de datos | PostgreSQL con PostGIS |
| Datos geoespaciales | Hibernate Spatial, JTS |
| Reportes | JasperReports (PDF), Apache POI (Excel) |
| Documentación | springdoc-openapi (Swagger UI) |
| Pruebas | JUnit 5, Mockito |
| Construcción | Maven |

## Arquitectura

```
Navegador ──> GuayabalSiSDE-Front (Jakarta Faces, WildFly)
                     │  HTTP + JSON
                     ▼
              GuayabalSiSDE (esta API, puerto 8081)
                     │  JPA / Hibernate Spatial
                     ▼
              PostgreSQL + PostGIS
```

El código sigue una estructura por capas: `controller` → `services` → `repository` → `entity`.

## Cómo ejecutarlo

**Requisitos:** Java 17 y PostgreSQL con la extensión PostGIS. Maven no hace falta: el repositorio incluye el *wrapper*.

1. Crea la base de datos y activa PostGIS:

   ```sql
   CREATE DATABASE "guayabalCultivosNew";
   \c guayabalCultivosNew
   CREATE EXTENSION postgis;
   ```

2. Opcional: carga los datos de ejemplo incluidos.

   ```bash
   pg_restore -U postgres -d guayabalCultivosNew guayabalCultivos.backup
   ```

3. Pon tu usuario y contraseña de PostgreSQL en `src/main/resources/application.properties`.

4. Arranca la API:

   ```bash
   ./mvnw spring-boot:run
   ```

La API queda en `http://localhost:8081` y la documentación en `http://localhost:8081/swagger-ui.html`.

## Recursos de la API

| Ruta | Recurso |
|---|---|
| `/area` | Áreas y sus polígonos |
| `/cultivo`, `/tipoCultivo` | Cultivos y tipos de cultivo |
| `/areaCultivo` | Asignación de cultivos por área |
| `/produccion` | Producción |
| `/planificacionCosecha` | Planificación de cosecha |
| `/riego` | Riego |
| `/agroquimico`, `/movimientoAgroquimico` | Agroquímicos e inventario |
| `/alertas` | Alertas del sistema |
| `/trabajador` | Trabajadores |
| `/Users`, `/Authorities` | Usuarios y roles |
| `/api/reportes` | Reportes en PDF |

El detalle de cada operación está en Swagger.

## Pruebas

```bash
./mvnw test
```

Hay 160 pruebas automatizadas de servicios y controladores, escritas con JUnit 5 y Mockito.

## Estado

El sistema sigue creciendo como parte de la tesis. Las funciones más recientes son las alertas automáticas, la planificación de cosecha y el control de inventario de agroquímicos.

## Autores

- **Osniel Noda Cabrera** — autor principal · [github.com/osnieln](https://github.com/osnieln)
- Gabriel Alberto Pérez Guerra — colaborador
