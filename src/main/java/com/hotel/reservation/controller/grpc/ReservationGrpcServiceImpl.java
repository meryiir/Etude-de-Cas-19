package com.hotel.reservation.controller.grpc;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.proto.*;
import com.hotel.reservation.service.ReservationService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@GrpcService
@Service
@RequiredArgsConstructor
public class ReservationGrpcServiceImpl extends ReservationServiceGrpc.ReservationServiceImplBase {
    private final ReservationService reservationService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void createReservation(CreateReservationRequest request, StreamObserver<ReservationResponse> responseObserver) {
        try {
            log.info("gRPC CreateReservation request received: dateDebut={}, dateFin={}, preferences={}", 
                    request.getDateDebut(), request.getDateFin(), request.getPreferences());
            
            if (request.getDateDebut() == null || request.getDateDebut().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("dateDebut est requis et ne peut pas être vide")
                        .asRuntimeException());
                return;
            }
            
            if (request.getDateFin() == null || request.getDateFin().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("dateFin est requis et ne peut pas être vide")
                        .asRuntimeException());
                return;
            }
            
            Reservation reservation = new Reservation();
            reservation.setDateDebut(LocalDate.parse(request.getDateDebut(), formatter));
            reservation.setDateFin(LocalDate.parse(request.getDateFin(), formatter));
            reservation.setPreferences(request.getPreferences() != null ? request.getPreferences() : "");

            Reservation created = reservationService.createReservation(reservation);

            ReservationResponse response = ReservationResponse.newBuilder()
                    .setId(created.getId())
                    .setDateDebut(created.getDateDebut().format(formatter))
                    .setDateFin(created.getDateFin().format(formatter))
                    .setPreferences(created.getPreferences() != null ? created.getPreferences() : "")
                    .build();

            log.info("gRPC CreateReservation success: id={}", created.getId());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (DateTimeParseException e) {
            log.error("Erreur de parsing de date dans CreateReservation", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Format de date invalide. Utilisez le format YYYY-MM-DD (ex: 2024-01-15)")
                    .withCause(e)
                    .asRuntimeException());
        } catch (RuntimeException e) {
            log.error("Erreur lors de la création de la réservation", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Erreur lors de la création de la réservation: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("Erreur inattendue dans CreateReservation", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Erreur inattendue: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void getReservation(GetReservationRequest request, StreamObserver<ReservationResponse> responseObserver) {
        try {
            log.info("gRPC GetReservation request received: id={}", request.getId());
            
            reservationService.getReservation(request.getId())
                    .ifPresentOrElse(
                            reservation -> {
                                ReservationResponse response = ReservationResponse.newBuilder()
                                        .setId(reservation.getId())
                                        .setDateDebut(reservation.getDateDebut().format(formatter))
                                        .setDateFin(reservation.getDateFin().format(formatter))
                                        .setPreferences(reservation.getPreferences() != null ? reservation.getPreferences() : "")
                                        .build();
                                log.info("gRPC GetReservation success: id={}", reservation.getId());
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                            },
                            () -> {
                                log.warn("gRPC GetReservation: Réservation non trouvée avec id={}", request.getId());
                                responseObserver.onError(Status.NOT_FOUND
                                        .withDescription("Réservation non trouvée avec l'ID: " + request.getId())
                                        .asRuntimeException());
                            }
                    );
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la réservation", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Erreur lors de la récupération de la réservation: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void updateReservation(UpdateReservationRequest request, StreamObserver<ReservationResponse> responseObserver) {
        try {
            log.info("gRPC UpdateReservation request received: id={}, dateDebut={}, dateFin={}, preferences={}", 
                    request.getId(), request.getDateDebut(), request.getDateFin(), request.getPreferences());
            
            if (request.getDateDebut() == null || request.getDateDebut().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("dateDebut est requis et ne peut pas être vide")
                        .asRuntimeException());
                return;
            }
            
            if (request.getDateFin() == null || request.getDateFin().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("dateFin est requis et ne peut pas être vide")
                        .asRuntimeException());
                return;
            }
            
            Reservation reservation = new Reservation();
            reservation.setDateDebut(LocalDate.parse(request.getDateDebut(), formatter));
            reservation.setDateFin(LocalDate.parse(request.getDateFin(), formatter));
            reservation.setPreferences(request.getPreferences() != null ? request.getPreferences() : "");

            Reservation updated = reservationService.updateReservation(request.getId(), reservation);

            ReservationResponse response = ReservationResponse.newBuilder()
                    .setId(updated.getId())
                    .setDateDebut(updated.getDateDebut().format(formatter))
                    .setDateFin(updated.getDateFin().format(formatter))
                    .setPreferences(updated.getPreferences() != null ? updated.getPreferences() : "")
                    .build();

            log.info("gRPC UpdateReservation success: id={}", updated.getId());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (DateTimeParseException e) {
            log.error("Erreur de parsing de date dans UpdateReservation", e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Format de date invalide. Utilisez le format YYYY-MM-DD (ex: 2024-01-15)")
                    .withCause(e)
                    .asRuntimeException());
        } catch (RuntimeException e) {
            log.error("Erreur lors de la mise à jour de la réservation", e);
            if (e.getMessage() != null && e.getMessage().contains("non trouvée")) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription(e.getMessage())
                        .withCause(e)
                        .asRuntimeException());
            } else {
                responseObserver.onError(Status.INTERNAL
                        .withDescription("Erreur lors de la mise à jour de la réservation: " + e.getMessage())
                        .withCause(e)
                        .asRuntimeException());
            }
        } catch (Exception e) {
            log.error("Erreur inattendue dans UpdateReservation", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Erreur inattendue: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void deleteReservation(DeleteReservationRequest request, StreamObserver<DeleteReservationResponse> responseObserver) {
        try {
            log.info("gRPC DeleteReservation request received: id={}", request.getId());
            
            reservationService.deleteReservation(request.getId());
            
            DeleteReservationResponse response = DeleteReservationResponse.newBuilder()
                    .setSuccess(true)
                    .build();
            
            log.info("gRPC DeleteReservation success: id={}", request.getId());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de la réservation", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Erreur lors de la suppression de la réservation: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}

