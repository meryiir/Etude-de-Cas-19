# Hotel Reservation API

API de réservation d'hôtels implémentée avec REST, SOAP, GraphQL et gRPC.

## Technologies

- **Backend**: Spring Boot 3.1.5
- **Base de données**: MySQL
- **APIs**: REST, SOAP, GraphQL, gRPC
- **Frontend**: React.js

## Structure du Projet

```
├── src/main/java/com/hotel/reservation/
│   ├── entity/              # Entités JPA (Client, Chambre, Réservation)
│   ├── repository/          # Repositories Spring Data
│   ├── service/             # Services métier
│   ├── controller/
│   │   ├── rest/            # Controllers REST
│   │   ├── soap/            # Endpoints SOAP
│   │   ├── graphql/         # Resolvers GraphQL
│   │   └── grpc/            # Services gRPC
│   └── config/              # Configurations
├── src/main/resources/
│   ├── application.properties
│   ├── schema.graphqls      # Schéma GraphQL
│   └── reservation.xsd      # Schéma XSD pour SOAP
├── src/main/proto/
│   └── reservation.proto    # Définition Protobuf pour gRPC
└── frontend/                # Application React

```

## Configuration

1. **Base de données MySQL**:
   - Créer une base de données MySQL
   - Configurer les identifiants dans `application.properties`

2. **Compilation**:
   ```bash
   mvn clean install
   ```

3. **Lancement**:
   ```bash
   mvn spring-boot:run
   ```

## Endpoints

### REST API
- `POST /api/reservations` - Créer une réservation
- `GET /api/reservations/{id}` - Consulter une réservation
- `GET /api/reservations` - Lister toutes les réservations
- `PUT /api/reservations/{id}` - Modifier une réservation
- `DELETE /api/reservations/{id}` - Supprimer une réservation

### SOAP API
- WSDL: `http://localhost:8080/soap/reservations.wsdl`
- Endpoint: `http://localhost:8080/soap`

#### Captures d'écran SOAP UI

(img/soapUI/1.png)

(img/soapUI/2.png)

(img/soapUI/3.png)

(img/soapUI/4.png)

### GraphQL API
- Endpoint: `http://localhost:8080/graphql`
- Playground: `http://localhost:8080/playground` (si activé)

#### Opérations GraphQL disponibles
- **Queries :**
  - `getAllReservations` - Récupère toutes les réservations
  - `getReservation(id: ID!)` - Récupère une réservation par ID
- **Mutations :**
  - `createReservation(input: ReservationInput!)` - Crée une nouvelle réservation
  - `updateReservation(id: ID!, input: ReservationInput!)` - Met à jour une réservation
  - `deleteReservation(id: ID!)` - Supprime une réservation

#### Utiliser GraphQL avec Postman
- Utiliser GraphQL Playground ou Postman pour tester les queries et mutations
- La collection Postman inclut une section **GraphQL** avec toutes les requêtes préconfigurées
- Voir le guide détaillé : [GUIDE_POSTMAN_GRAPHQL.md](GUIDE_POSTMAN_GRAPHQL.md)

### gRPC API
- Port: `9090`
- Service: `ReservationService`

## Postman Collection

Vous pouvez importer la collection Postman fournie (`Hotel_Reservation_API.postman_collection.json`) pour tester les différents endpoints.

La collection inclut :
- **Réservations** : Requêtes REST pour les opérations CRUD
- **GraphQL** : Queries et mutations GraphQL pour tester l'API GraphQL

Voir le guide détaillé pour GraphQL : [GUIDE_POSTMAN_GRAPHQL.md](GUIDE_POSTMAN_GRAPHQL.md)

### Captures d'écran Postman

![Postman Screenshot 1](img/postman/1.png)

![Postman Screenshot 2](img/postman/2.png)

![Postman Screenshot 3](img/postman/3.png)

![Postman Screenshot 4](img/postman/4.png)

![Postman Screenshot 5](img/postman/5.png)

## Frontend

Pour lancer le frontend React:

```bash
cd frontend
npm install
npm start
```

Le frontend sera accessible sur `http://localhost:3000`

