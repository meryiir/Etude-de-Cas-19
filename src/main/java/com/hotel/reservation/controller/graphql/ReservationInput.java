package com.hotel.reservation.controller.graphql;

import lombok.Data;

@Data
public class ReservationInput {
    private String dateDebut;
    private String dateFin;
    private String preferences;
}

