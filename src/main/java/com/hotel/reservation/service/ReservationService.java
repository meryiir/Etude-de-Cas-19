package com.hotel.reservation.service;

import com.hotel.reservation.entity.Chambre;
import com.hotel.reservation.entity.Client;
import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.repository.ChambreRepository;
import com.hotel.reservation.repository.ClientRepository;
import com.hotel.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ChambreRepository chambreRepository;

    @Transactional
    public Reservation createReservation(Reservation reservation) {
        // Charger les entités Client et Chambre par ID si elles existent
        if (reservation.getClient() != null && reservation.getClient().getId() != null) {
            Client client = clientRepository.findById(reservation.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + reservation.getClient().getId()));
            reservation.setClient(client);
        }
        
        if (reservation.getChambre() != null && reservation.getChambre().getId() != null) {
            Chambre chambre = chambreRepository.findById(reservation.getChambre().getId())
                    .orElseThrow(() -> new RuntimeException("Chambre non trouvée avec l'ID: " + reservation.getChambre().getId()));
            reservation.setChambre(chambre);
        }
        
        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> getReservation(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation updateReservation(Long id, Reservation reservation) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        // Charger les entités Client et Chambre par ID si elles existent
        if (reservation.getClient() != null && reservation.getClient().getId() != null) {
            Client client = clientRepository.findById(reservation.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + reservation.getClient().getId()));
            existingReservation.setClient(client);
        }
        
        if (reservation.getChambre() != null && reservation.getChambre().getId() != null) {
            Chambre chambre = chambreRepository.findById(reservation.getChambre().getId())
                    .orElseThrow(() -> new RuntimeException("Chambre non trouvée avec l'ID: " + reservation.getChambre().getId()));
            existingReservation.setChambre(chambre);
        }
        
        existingReservation.setDateDebut(reservation.getDateDebut());
        existingReservation.setDateFin(reservation.getDateFin());
        existingReservation.setPreferences(reservation.getPreferences());
        
        return reservationRepository.save(existingReservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}

