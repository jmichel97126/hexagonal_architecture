# Demo Hexagonal Architecture (Spring Boot 4 + Java 25)

Projet volontairement simple pour comprendre les rôles dans une architecture hexagonale (Ports & Adapters).

## Structure

```text
src/main/java/com/example/hexagonal
├── domain
│   └── model
│       └── Order.java
├── application
│   ├── port
│   │   ├── in
│   │   │   ├── CreateOrderUseCase.java
│   │   │   └── GetOrderUseCase.java
│   │   └── out
│   │       ├── LoadOrderPort.java
│   │       └── SaveOrderPort.java
│   └── service
│       └── OrderService.java
└── adapter
    ├── in
    │   └── web
    │       ├── OrderController.java
    │       └── dto
    └── out
        └── persistence
            └── InMemoryOrderRepository.java
```

## Lecture de l'architecture

- **Domain**: règles métier pures (`Order`), sans dépendance Spring.
- **Application**: cas d'usage (`CreateOrderUseCase`, `GetOrderUseCase`) + orchestration (`OrderService`).
- **Ports sortants (out)**: contrats d'accès aux ressources externes (ici persistance).
- **Adapters sortants**: implémentations concrètes des ports (`InMemoryOrderRepository`).
- **Adapters entrants**: REST (`OrderController`) qui traduit HTTP -> cas d'usage.

Le coeur métier dépend seulement d'interfaces (ports), jamais des détails techniques.

## In vs Out (simple à retenir)

### IN = ce qui ENTRE dans l'application

- **Port IN**: ce que l'application expose comme capacités métier.
  - Exemples: `CreateOrderUseCase`, `GetOrderUseCase`
- **Adapter IN**: la techno qui appelle ces capacités.
  - Exemple ici: `OrderController` (HTTP REST)

👉 Question à se poser: "Qui déclenche mon cas d'usage ?"

Dans ce projet, un client HTTP appelle `/orders`, le contrôleur convertit la requête en `CreateOrderCommand`, puis appelle le port IN.

### OUT = ce qui SORT de l'application

- **Port OUT**: ce dont le cas d'usage a besoin pour agir sur l'extérieur.
  - Exemples: `SaveOrderPort`, `LoadOrderPort`
- **Adapter OUT**: implémentation technique de ce besoin.
  - Exemple ici: `InMemoryOrderRepository`

👉 Question à se poser: "De quoi mon use case a besoin en externe (DB, API, message broker) ?"

Dans ce projet, le use case ne connaît pas la persistance concrète. Il appelle seulement un port OUT; l'implémentation peut être remplacée (mémoire, PostgreSQL, Mongo...) sans modifier le domaine.

### Sens des dépendances

- Les dépendances de code vont **vers l'intérieur** (adapter -> port -> domaine).
- Le domaine ne dépend jamais des frameworks ou de la base.
- Spring sert à câbler les implémentations, pas à porter la logique métier.

## Lancer le projet

```bash
mvn spring-boot:run
```

## API de démonstration

### 1) Créer une commande

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName":"Alice","totalAmount":99.90}'
```

Réponse:

```json
{
  "id": "..."
}
```

### 2) Lire une commande

```bash
curl http://localhost:8080/orders/{id}
```

## Comment l'expliquer simplement

1. Le contrôleur ne contient pas de métier: il appelle un **use case**.
2. Le use case ne sait pas comment on persiste: il parle à un **port**.
3. L'adapter concret implémente ce port (in-memory aujourd'hui, base SQL demain).
4. Si on change la techno (REST, Kafka, DB), le domaine et les use cases restent stables.
