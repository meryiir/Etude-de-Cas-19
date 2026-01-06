package com.hotel.reservation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.namespace.QName;

@Slf4j
public class SoapExceptionHandler extends SoapFaultMappingExceptionResolver {

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        log.error("=== SOAP FAULT OCCURRED ===");
        log.error("Endpoint: {}", endpoint != null ? endpoint.getClass().getName() : "null");
        log.error("Exception type: {}", ex.getClass().getName());
        log.error("Exception message: {}", ex.getMessage());
        log.error("Stack trace:", ex);
        
        SoapFaultDetail detail = fault.addFaultDetail();
        detail.addFaultDetailElement(new QName("http://hotel.com/reservation/soap", "error"));
        detail.addFaultDetailElement(new QName("http://hotel.com/reservation/soap", "message"));
        
        log.error("=== END SOAP FAULT ===");
    }
}

