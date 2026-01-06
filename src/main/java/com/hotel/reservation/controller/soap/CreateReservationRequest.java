package com.hotel.reservation.controller.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"dateDebut", "dateFin", "preferences"}, namespace = "http://hotel.com/reservation/soap")
@XmlRootElement(name = "createReservationRequest", namespace = "http://hotel.com/reservation/soap")
public class CreateReservationRequest {
    @XmlElement(required = true, namespace = "http://hotel.com/reservation/soap")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateDebut;
    
    @XmlElement(required = true, namespace = "http://hotel.com/reservation/soap")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateFin;
    
    @XmlElement(namespace = "http://hotel.com/reservation/soap")
    private String preferences;

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}

