# Luxury Watches REST API

REST API sviluppata con Spring Boot per la gestione di un catalogo di orologi di lusso.

Il progetto include CRUD completo, validazione dati, gestione errori centralizzata e ricerca avanzata con filtri dinamici.

## Tech Stack
- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Maven
- Logback

## Architettura
Controller -> Service -> Repository

Utilizzo di DTO per separare request/response dal model database.

## Funzionalità
- CRUD completo watches
- DTO pattern
- Validazione input con Jakarta Validation
- Exception handling centralizzato
- Logging operazioni principali
- Ricerca avanzata con JPA Specification
- Configurazione tramite variabili ambiente

## Entity Watch
### Campi principali:
- Brand
- Model
- Case Material
- Strap Material
- Movement Type
- Water Resistance
- Dimensions
- Dial Color
- Crystal Material
- Complications
- Power Reserve
- Price

## Search Filters
### L'endpoint /api/watches/search supporta filtri opzionali combinabili:
- brand
- model
- materials
- movementType
- waterResistance
- dimensions
- powerReserve
- maxPrice

## Database
- Database: PostgreSQL
- Nome database: LuxuryWatches
> Nota: Alcuni dati numerici (come prezzi o misure) sono memorizzati come VARCHAR invece che come tipi numerici. Questo può influenzare operazioni come ordinamento o calcoli diretti nel database.
