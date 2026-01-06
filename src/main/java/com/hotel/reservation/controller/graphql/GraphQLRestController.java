package com.hotel.reservation.controller.graphql;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/graphql")
@CrossOrigin(origins = "*")
public class GraphQLRestController {

    private final GraphQL graphQL;

    @Autowired
    public GraphQLRestController(ReservationGraphQLController queryResolver) throws IOException {
        
        // Charger le schéma GraphQL depuis le fichier
        ClassPathResource schemaResource = new ClassPathResource("schema.graphqls");
        String schemaString = new String(schemaResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        
        log.info("Chargement du schéma GraphQL depuis schema.graphqls");
        
        // Créer le parser de schéma avec les resolvers
        SchemaParser schemaParser = SchemaParser.newParser()
                .schemaString(schemaString)
                .resolvers(queryResolver)
                .build();
        
        GraphQLSchema schema = schemaParser.makeExecutableSchema();
        
        this.graphQL = GraphQL.newGraphQL(schema).build();
        
        log.info("GraphQL endpoint configuré sur /graphql");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> graphql(@RequestBody Map<String, Object> request) {
        try {
            String query = (String) request.get("query");
            @SuppressWarnings("unchecked")
            Map<String, Object> variables = (Map<String, Object>) request.getOrDefault("variables", new HashMap<>());
            
            log.debug("Requête GraphQL reçue: {}", query);
            
            ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                    .query(query)
                    .variables(variables)
                    .build();
            
            ExecutionResult executionResult = graphQL.execute(executionInput);
            
            Map<String, Object> result = new HashMap<>();
            if (executionResult.getErrors().isEmpty()) {
                result.put("data", executionResult.getData());
            } else {
                result.put("data", executionResult.getData());
                result.put("errors", executionResult.getErrors());
                log.error("Erreurs GraphQL: {}", executionResult.getErrors());
            }
            
            return result;
        } catch (Exception e) {
            log.error("Erreur lors de l'exécution de la requête GraphQL", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("errors", List.of(Map.of(
                    "message", e.getMessage(),
                    "extensions", Map.of("code", "INTERNAL_ERROR")
            )));
            return errorResult;
        }
    }

    @GetMapping
    public String graphqlInfo() {
        return "GraphQL endpoint disponible. Utilisez POST /graphql avec une requête GraphQL.";
    }
}

