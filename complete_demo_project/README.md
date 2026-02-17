# Demo avancée - Architecture Hexagonale (Spring Boot 4 + Java 25)

Ce projet illustre une implémentation **pragmatique et avancée** de l'architecture hexagonale (Ports & Adapters), avec un domaine métier isolé des frameworks.

## Objectif pédagogique

Montrer comment :
- protéger le domaine des dépendances techniques,
- orchestrer les cas d'usage via des ports d'entrée,
- brancher plusieurs adapters sortants (JPA, horloge, génération d'ID, événements),
- gérer des règles métier, erreurs métier et validation API,
- tester séparément le domaine et l'intégration applicative.

## Stack technique

- Java 25
- Spring Boot 4
- Spring Web
- Spring Data JPA
- H2 (mémoire)
- Jakarta Validation
- JUnit 5 + SpringBootTest (HTTP réel)

## Structure du projet

```text
src/main/java/com/example/hexagonal
├── domain
│   ├── event
│   ├── exception
│   └── model
├── application
│   ├── command
│   ├── exception
│   ├── port
│   │   ├── in
│   │   └── out
│   ├── query
│   └── service
└── adapter
    ├── in
    │   └── web
    └── out
        ├── event
        ├── persistence
        └── system
```

## Notions avancées couvertes

1. **Domaine riche (pas anémique)**
   - `Order` encapsule les invariants (`cancel` interdit en double, minimum 1 item).
   - `Money`, `OrderId`, `CustomerId` sont des value objects typés.

2. **Ports explicites par intention**
   - Entrée: `CreateOrderUseCase`, `GetOrderUseCase`, `CancelOrderUseCase`.
   - Sortie: `OrderRepositoryPort`, `DomainEventPublisherPort`, `ClockPort`, `IdGeneratorPort`.

3. **Événements de domaine**
   - `OrderCreatedEvent` émis par l'agrégat.
   - Publication externalisée par un port sortant (adapter `LoggingDomainEventPublisherAdapter`).

4. **Concurrence optimiste**
   - Versionnage JPA (`@Version`) dans `OrderJpaEntity`.
   - Le domaine expose un `version` consommé par la couche de persistance.

5. **Frontière API robuste**
   - Validation d'entrée avec Jakarta Validation.
   - Gestion centralisée des erreurs avec `ProblemDetail` (`ApiExceptionHandler`).

6. **Tests multi-niveaux**
   - Unit test domaine pur (`OrderTest`).
   - Test d'intégration API (`OrderControllerIntegrationTest`).

## Flux d'une requête (create order)

1. `POST /api/orders` arrive sur l'adapter web.
2. Mapping DTO -> `CreateOrderCommand`.
3. `OrderApplicationService` exécute le cas d'usage.
4. Le domaine crée l'agrégat et ses événements.
5. Sauvegarde via `OrderRepositoryPort` -> adapter JPA.
6. Publication des événements via `DomainEventPublisherPort`.
7. Retour d'une `OrderResponse`.

## Lancer le projet

```bash
mvn spring-boot:run
```

## Exemples d'API

### Créer une commande

```http
POST /api/orders
Content-Type: application/json

{
  "customerId": "customer-42",
  "items": [
    { "productCode": "BOOK-1", "quantity": 2, "unitPrice": 12.50, "currency": "EUR" },
    { "productCode": "PEN-RED", "quantity": 1, "unitPrice": 1.90, "currency": "EUR" }
  ]
}
```

### Récupérer une commande

```http
GET /api/orders/{orderId}
```

### Annuler une commande

```http
POST /api/orders/{orderId}/cancel
```

## Points d'extension (pour aller plus loin)

- Remplacer le publisher logs par un outbox pattern transactionnel.
- Ajouter CQRS (read model dédié) pour des lectures à grande échelle.
- Introduire des policies/domain services pour les règles cross-aggregate.
- Ajouter authN/authZ par adapter entrant (sans contaminer le domaine).

## Commandes utiles

```bash
mvn test
mvn spring-boot:run
```
