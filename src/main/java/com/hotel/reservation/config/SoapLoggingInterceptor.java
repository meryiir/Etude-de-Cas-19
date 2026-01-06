package com.hotel.reservation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapEnvelope;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapHeader;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

@Slf4j
public class SoapLoggingInterceptor implements EndpointInterceptor {

    @Override
    public boolean handleRequest(org.springframework.ws.context.MessageContext messageContext, Object endpoint) throws Exception {
        log.info("=== SOAP REQUEST RECEIVED ===");
        
        if (messageContext.getRequest() instanceof SoapMessage) {
            SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();
            SoapEnvelope envelope = soapMessage.getEnvelope();
            
            try {
                log.info("SOAP Message Type: {}", soapMessage.getClass().getName());
                
                SoapHeader header = envelope.getHeader();
                if (header != null) {
                    String headerXml = convertToString(header.getSource());
                    log.info("SOAP Header: {}", headerXml);
                } else {
                    log.info("SOAP Header: (empty)");
                }
                
                SoapBody body = envelope.getBody();
                if (body != null) {
                    String bodyXml = convertToString(body.getSource());
                    log.info("SOAP Body: {}", bodyXml);
                } else {
                    log.warn("SOAP Body: (empty or null)");
                }
                
                String fullEnvelope = convertToString(envelope.getSource());
                log.info("Full SOAP Envelope: {}", fullEnvelope);
                
            } catch (Exception e) {
                log.error("Error logging SOAP message: {}", e.getMessage(), e);
            }
        } else {
            log.warn("Request is not a SoapMessage: {}", messageContext.getRequest().getClass().getName());
            try {
                String requestXml = convertToString(messageContext.getRequest().getPayloadSource());
                log.info("Request XML: {}", requestXml);
            } catch (Exception e) {
                log.error("Error converting request to string: {}", e.getMessage(), e);
            }
        }
        
        log.info("=== END SOAP REQUEST ===");
        return true;
    }

    @Override
    public boolean handleResponse(org.springframework.ws.context.MessageContext messageContext, Object endpoint) throws Exception {
        log.info("=== SOAP RESPONSE ===");
        if (messageContext.getResponse() instanceof SoapMessage) {
            SoapMessage soapMessage = (SoapMessage) messageContext.getResponse();
            try {
                String responseXml = convertToString(soapMessage.getEnvelope().getSource());
                log.info("SOAP Response: {}", responseXml);
            } catch (Exception e) {
                log.error("Error logging SOAP response: {}", e.getMessage(), e);
            }
        }
        log.info("=== END SOAP RESPONSE ===");
        return true;
    }

    @Override
    public boolean handleFault(org.springframework.ws.context.MessageContext messageContext, Object endpoint) throws Exception {
        log.error("=== SOAP FAULT ===");
        if (messageContext.getResponse() instanceof SoapMessage) {
            SoapMessage soapMessage = (SoapMessage) messageContext.getResponse();
            try {
                String faultXml = convertToString(soapMessage.getEnvelope().getSource());
                log.error("SOAP Fault: {}", faultXml);
            } catch (Exception e) {
                log.error("Error logging SOAP fault: {}", e.getMessage(), e);
            }
        }
        log.error("=== END SOAP FAULT ===");
        return true;
    }

    @Override
    public void afterCompletion(org.springframework.ws.context.MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
        if (ex != null) {
            log.error("=== SOAP REQUEST COMPLETION WITH EXCEPTION ===");
            log.error("Exception type: {}", ex.getClass().getName());
            log.error("Exception message: {}", ex.getMessage(), ex);
        }
    }

    private String convertToString(javax.xml.transform.Source source) throws TransformerException {
        if (source == null) {
            return "(null)";
        }
        StringWriter writer = new StringWriter();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.newTransformer().transform(source, new StreamResult(writer));
        return writer.toString();
    }
}

