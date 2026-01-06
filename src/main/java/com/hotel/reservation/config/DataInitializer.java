package com.hotel.reservation.config;

import com.hotel.reservation.entity.Chambre;
import com.hotel.reservation.entity.Client;
import com.hotel.reservation.repository.ChambreRepository;
import com.hotel.reservation.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final ClientRepository clientRepository;
    private final ChambreRepository chambreRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Vérifier si des données existent déjà
        if (clientRepository.count() == 0) {
            log.info("Initialisation des données de test...");
            
            // Créer des clients de test
            Client client1 = new Client();
            client1.setNom("Dupont");
            client1.setPrenom("Jean");
            client1.setEmail("jean.dupont@example.com");
            client1.setTelephone("+33123456789");
            clientRepository.save(client1);
            
            Client client2 = new Client();
            client2.setNom("Martin");
            client2.setPrenom("Marie");
            client2.setEmail("marie.martin@example.com");
            client2.setTelephone("+33987654321");
            clientRepository.save(client2);
            
            log.info("Clients créés : {}", clientRepository.count());
            
            // Créer des chambres de test
            Chambre chambre1 = new Chambre();
            chambre1.setType("Standard");
            chambre1.setPrix(100.0);
            chambre1.setDisponible(true);
            chambreRepository.save(chambre1);
            
            Chambre chambre2 = new Chambre();
            chambre2.setType("Deluxe");
            chambre2.setPrix(200.0);
            chambre2.setDisponible(true);
            chambreRepository.save(chambre2);
            
            Chambre chambre3 = new Chambre();
            chambre3.setType("Suite");
            chambre3.setPrix(350.0);
            chambre3.setDisponible(true);
            chambreRepository.save(chambre3);
            
            log.info("Chambres créées : {}", chambreRepository.count());
            log.info("Initialisation terminée !");
        } else {
            log.info("Des données existent déjà. Initialisation ignorée.");
        }
    }
}

