package com.hotel.reservation.controller.graphql;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.service.ReservationService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationGraphQLController implements GraphQLQueryResolver, GraphQLMutationResolver {
    private final ReservationService reservationService;

    public Reservation getReservation(Long id) {
        return reservationService.getReservation(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
    }

    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    public Reservation createReservation(ReservationInput input) {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(java.time.LocalDate.parse(input.getDateDebut()));
        reservation.setDateFin(java.time.LocalDate.parse(input.getDateFin()));
        reservation.setPreferences(input.getPreferences());
        return reservationService.createReservation(reservation);
    }

    public Reservation updateReservation(Long id, ReservationInput input) {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(java.time.LocalDate.parse(input.getDateDebut()));
        reservation.setDateFin(java.time.LocalDate.parse(input.getDateFin()));
        reservation.setPreferences(input.getPreferences());
        return reservationService.updateReservation(id, reservation);
    }

    public Boolean deleteReservation(Long id) {
        reservationService.deleteReservation(id);
        return true;
    }
}

