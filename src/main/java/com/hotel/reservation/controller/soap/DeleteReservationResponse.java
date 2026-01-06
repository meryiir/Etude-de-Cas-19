package com.hotel.reservation.controller.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"success"}, namespace = "http://hotel.com/reservation/soap")
@XmlRootElement(name = "deleteReservationResponse", namespace = "http://hotel.com/reservation/soap")
public class DeleteReservationResponse {
    @XmlElement(namespace = "http://hotel.com/reservation/soap")
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}

