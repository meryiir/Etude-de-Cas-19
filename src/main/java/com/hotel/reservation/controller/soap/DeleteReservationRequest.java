package com.hotel.reservation.controller.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"id"}, namespace = "http://hotel.com/reservation/soap")
@XmlRootElement(name = "deleteReservationRequest", namespace = "http://hotel.com/reservation/soap")
public class DeleteReservationRequest {
    @XmlElement(required = true, namespace = "http://hotel.com/reservation/soap")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

