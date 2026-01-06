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

![SOAP UI Screenshot 1](img/soapUI/1.png)

![SOAP UI Screenshot 2](img/soapUI/2.png)

![SOAP UI Screenshot 3](img/soapUI/3.png)

![SOAP UI Screenshot 4](img/soapUI/4.png)

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

#### Captures d'écran GraphQL

![GraphQL Screenshot 1](img/graphql/1.png)

![GraphQL Screenshot 2](img/graphql/2.png)

![GraphQL Screenshot 3](img/graphql/3.png)

![GraphQL Screenshot 4](img/graphql/4.png)

### gRPC API
- Port: `9090`
- Service: `ReservationService`
- Fichier proto: `src/main/proto/reservation.proto`

#### Méthodes RPC disponibles
- `CreateReservation` - Créer une nouvelle réservation
- `GetReservation` - Récupérer une réservation par ID
- `UpdateReservation` - Mettre à jour une réservation
- `DeleteReservation` - Supprimer une réservation

#### Captures d'écran gRPC (BloomRPC)

![gRPC Screenshot 1](img/grpc/1.png)

![gRPC Screenshot 2](img/grpc/2.png)

![gRPC Screenshot 3](img/grpc/3.png)

![gRPC Screenshot 4](img/grpc/4.png)

## Postman Collection

Vous pouvez importer les collections Postman fournies pour tester les différents endpoints :

- `Hotel_Reservation_API.postman_collection.json` - Collection REST API
- `Hotel_Reservation_GraphQL.postman_collection.json` - Collection GraphQL API
- `Hotel_Reservation_gRPC.postman_collection.json` - Collection gRPC API

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

