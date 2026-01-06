package com.hotel.reservation.controller.soap;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Slf4j
@Endpoint
@RequiredArgsConstructor
public class ReservationEndpoint {
    private static final String NAMESPACE_URI = "http://hotel.com/reservation/soap";
    private final ReservationService reservationService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createReservationRequest")
    @ResponsePayload
    public CreateReservationResponse createReservation(@RequestPayload CreateReservationRequest request) {
        log.info("=== CREATE RESERVATION SOAP REQUEST ===");
        try {
            log.info("Received request - dateDebut: {}, dateFin: {}, preferences: {}", 
                    request.getDateDebut(), request.getDateFin(), request.getPreferences());
            
            if (request == null) {
                log.error("Request is null");
                throw new IllegalArgumentException("Request cannot be null");
            }
            
            if (request.getDateDebut() == null) {
                log.error("dateDebut is null");
                throw new IllegalArgumentException("dateDebut cannot be null");
            }
            
            if (request.getDateFin() == null) {
                log.error("dateFin is null");
                throw new IllegalArgumentException("dateFin cannot be null");
            }
            
            Reservation reservation = new Reservation();
            reservation.setDateDebut(request.getDateDebut());
            reservation.setDateFin(request.getDateFin());
            reservation.setPreferences(request.getPreferences());
            
            log.info("Creating reservation with dates: {} to {}", reservation.getDateDebut(), reservation.getDateFin());
            Reservation created = reservationService.createReservation(reservation);
            log.info("Reservation created successfully with ID: {}", created.getId());
            
            CreateReservationResponse response = new CreateReservationResponse();
            response.setId(created.getId());
            response.setDateDebut(created.getDateDebut());
            response.setDateFin(created.getDateFin());
            response.setPreferences(created.getPreferences());
            
            log.info("=== CREATE RESERVATION SUCCESS ===");
            return response;
        } catch (Exception e) {
            log.error("=== ERROR IN CREATE RESERVATION ===");
            log.error("Exception type: {}", e.getClass().getName());
            log.error("Exception message: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getReservationRequest")
    @ResponsePayload
    public GetReservationResponse getReservation(@RequestPayload GetReservationRequest request) {
        GetReservationResponse response = new GetReservationResponse();
        reservationService.getReservation(request.getId()).ifPresent(reservation -> {
            response.setId(reservation.getId());
            response.setDateDebut(reservation.getDateDebut());
            response.setDateFin(reservation.getDateFin());
            response.setPreferences(reservation.getPreferences());
        });
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateReservationRequest")
    @ResponsePayload
    public UpdateReservationResponse updateReservation(@RequestPayload UpdateReservationRequest request) {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(request.getDateDebut());
        reservation.setDateFin(request.getDateFin());
        reservation.setPreferences(request.getPreferences());
        
        Reservation updated = reservationService.updateReservation(request.getId(), reservation);
        
        UpdateReservationResponse response = new UpdateReservationResponse();
        response.setId(updated.getId());
        response.setDateDebut(updated.getDateDebut());
        response.setDateFin(updated.getDateFin());
        response.setPreferences(updated.getPreferences());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteReservationRequest")
    @ResponsePayload
    public DeleteReservationResponse deleteReservation(@RequestPayload DeleteReservationRequest request) {
        reservationService.deleteReservation(request.getId());
        DeleteReservationResponse response = new DeleteReservationResponse();
        response.setSuccess(true);
        return response;
    }
}

