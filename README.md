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
- JUnit 5
- Mockito
- MockMvc
- Spring Security
- JSON Web Token (JWT)

---

## Authentication & Security

Il progetto implementa un sistema di autenticazione e autorizzazione basato su Spring Security e JWT (JSON Web Token).

### Meccanismo di sicurezza

L’API è protetta tramite autenticazione stateless:

- L’utente si registra tramite endpoint pubblico  
- Effettua login con username e password  
- Riceve un JWT token  
- Il token viene utilizzato per autorizzare le richieste successive  

---

### Flusso di autenticazione

1. Register  
   `POST /auth/register`  
   Password salvata in formato hash (BCrypt)

2. Login  
   `POST /auth/login`  
   Verifica credenziali tramite AuthenticationManager  
   Generazione JWT token  

3. API Requests protette  
``` md id="a1b2c3"
Header richiesto:

Authorization: Bearer <JWT_TOKEN>
```

Il token viene validato ad ogni richiesta tramite filtro JWT

### Authentication Errors

In caso di credenziali non valide, l'API restituisce:

- HTTP 401 Unauthorized
- Error response standardizzata tramite GlobalExceptionHandler

Per motivi di sicurezza non viene specificato se l'errore dipende da username inesistente o password errata.

---

### JWT Features

- Token firmato con chiave segreta (HMAC SHA)
- Expiration time configurabile
- Stateless authentication (nessuna sessione server-side)
- Estrazione username dal token
- Validazione firma e scadenza

---

### Authorization (Role-based access control)

Il sistema supporta ruoli utente:

- USER
- ADMIN

Regole di accesso:

- GET /api/watches/** → USER + ADMIN  
- POST /api/watches/** → ADMIN  
- PATCH /api/watches/** → ADMIN  
- DELETE /api/watches/** → ADMIN  

---

### Spring Security Configuration

- Stateless session management
- CSRF disabled (REST API)
- HTTP Basic e Form Login disabilitati
- JWT Filter personalizzato per autenticazione
- SecurityContext popolato tramite token

---

### Swagger Authentication

Per testare endpoint protetti tramite Swagger UI:

1. Effettuare login tramite `/auth/login`
2. Copiare il token JWT ricevuto
3. Cliccare su Authorize in Swagger UI
4. Inserire:
``` md id="d4e5f6"
Bearer <JWT_TOKEN>
```

---

## API Documentation (Swagger / OpenAPI)

Il progetto integra Swagger/OpenAPI per la documentazione e il test delle API.

### Accesso alla documentazione:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Funzionalità:
- Esplorazione interattiva degli endpoint
- Test API direttamente dal browser
- Documentazione automatica dei DTO
- Supporto a query params, pagination e validation

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
- Exception handling centralizzato con error response uniforme (GlobalExceptionHandler)
- Logging operazioni principali
- Ricerca avanzata con JPA Specification
- Paginazione e sorting con Pageable
- Whitelist dei campi ordinabili (security su sort)
- Sanitizzazione parametri di paginazione
- Configurazione tramite variabili ambiente
- Error response standardizzato (timestamp, status, error, message, path, method, validation errors)
- Gestione uniforme di validation errors, type mismatch e runtime exceptions
- Unit test su mapper e service layer
- Controller test con MockMvc
- Documentazione API con Swagger/OpenAPI

---

## Testing

Il progetto include test automatizzati per verificare:

### Mapper Tests
- conversione Entity → DTO
- update parziale con campi non null
- preservazione valori esistenti quando il DTO contiene null

### Service Tests
- recupero watch tramite ID
- ricerca paginata
- creazione watch
- aggiornamento watch
- eliminazione watch
- gestione eccezioni (`404 NOT FOUND`, `400 BAD REQUEST`)
- registrazione nuovo utente
- gestione username duplicato
- login con credenziali valide
- gestione credenziali non valide

### Controller Tests
- endpoint REST principali
- validazione request body
- gestione error response
- verifica status HTTP e payload JSON tramite MockMvc

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

## List All Watches (Paginated)

### Endpoint:
`GET /api/watches`

Restituisce una lista paginata di orologi con un DTO semplificato (`WatchListDTO`), senza tutti i dettagli completi.

### Differenza rispetto a search:
- Nessun filtro applicato
- Output semplificato
- Usato per listing generale
- Supporta paginazione e sorting

### Parametri:
- page (default: 0)
- size (default: 10)
- sort (es: `brand,asc`)

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

## Error Handling

Le eccezioni vengono gestite centralmente tramite `GlobalExceptionHandler`.

### Gestione supportata:
- Validation errors (`MethodArgumentNotValidException`)
- Invalid parameter type (`MethodArgumentTypeMismatchException`)
- Authentication errors (`BadCredentialsException`)
- Business exceptions (`ResponseStatusException`)
- Generic runtime exceptions (`Exception`)

### Struttura standard error response:
```json
{
  "timestamp": "2026-05-20T12:00:00",
  "status": 400,
  "error": "400 BAD_REQUEST",
  "message": "Validation failed",
  "path": "/api/watches",
  "method": "POST",
  "errors": {
    "price": "must be greater than 0"
  }
}
```

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
- I controller delegano completamente la business logic al service layer
- Le response API sono mantenute consistenti tramite DTO dedicati

---

## Docker Setup

Il progetto è completamente containerizzato tramite Docker e Docker Compose.

### Avvio dell'applicazione

docker compose up --build

### Servizi inclusi
- Spring Boot REST API
- PostgreSQL database
- Inizializzazione automatica del database tramite init.sql

---

## Environment Variables

La configurazione del database è gestita tramite file .env:

- DB_HOST
- DB_PORT
- DB_NAME
- DB_USERNAME
- DB_PASSWORD

---

## Database Initialization

All'avvio del container PostgreSQL viene eseguito automaticamente lo script:

docker/init/init.sql

Questo script crea e popola la tabella luxury_watches con dati di esempio.