# Luxury Watches REST API

REST API sviluppata con Spring Boot per la gestione di un catalogo di orologi di lusso.

Il progetto implementa CRUD completo, ricerca avanzata con filtri dinamici, paginazione, validazione input e separazione pulita delle responsabilità tra controller, service e repository.

---

## Tech Stack
- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Maven
- SLF4J + Logback
- Jakarta Validation

---

## Architettura

Controller → Service → Repository

Il progetto segue un’architettura a livelli con separazione chiara delle responsabilità:

- **Controller**: espone le API REST
- **Service**: contiene la business logic
- **Repository**: accesso ai dati tramite Spring Data JPA

Pattern utilizzati:
- DTO per separare entity e API contract
- Mapper centralizzato per conversioni Entity ↔ DTO
- Specification pattern per query dinamiche
- Validator per sanitizzazione input (Pageable, sort, ecc.)
- PaginatedResponse per standardizzare le risposte paginate

---

## Funzionalità

- CRUD completo watches
- DTO pattern (Create / Update / List / Details)
- Validazione input con Jakarta Validation
- Exception handling centralizzato (REST errors uniformi)
- Logging operazioni principali
- Ricerca avanzata con JPA Specification
- Paginazione e sorting con Pageable
- Whitelist dei campi ordinabili (security su sort)
- Sanitizzazione parametri di paginazione
- Configurazione tramite variabili ambiente

---

## Entity Watch

### Campi principali:
- Brand
- Model
- Case Material
- Strap Material
- Movement Type
- Water Resistance
- Case Dimensions
- Dial Color
- Crystal Material
- Complications
- Power Reserve
- Price

---

## Search

### Endpoint:
`GET /api/watches/search`

La ricerca utilizza:
- DTO dedicato (`WatchSearchDTO`)
- Specification builder centralizzato
- Filtri opzionali combinabili
- Paginazione tramite Pageable

### Filtri supportati:
- brand
- model
- caseMaterial
- strapMaterial
- movementType
- waterResistance
- caseDiameter
- caseThickness
- bandWidth
- dialColor
- crystalMaterial
- complications
- powerReserve
- maxPrice

---

## Pagination

Le risposte paginate sono standardizzate tramite:

- `PaginatedResponse<T>`
- metadata inclusi:
  - page
  - size
  - totalElements
  - totalPages
  - content

---

## Database

- Database: PostgreSQL
- Nome database: LuxuryWatches

> Nota: Alcuni campi numerici sono stati inizialmente modellati come stringhe per semplicità, ma la struttura attuale è stata progressivamente normalizzata verso tipi più appropriati dove necessario.

---

## Note architetturali

- Il mapping Entity → DTO è centralizzato nel `WatchMapper`
- La logica di query dinamica è separata nel `WatchSearchSpecification`
- La validazione della paginazione è gestita da `PageableValidator`
- Il service layer non contiene logica di costruzione query complessa