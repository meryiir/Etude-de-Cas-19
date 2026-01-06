package com.hotel.reservation.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@Slf4j
@Component
@Order(1)
public class SoapRequestLoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("SoapRequestLoggingFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String path = httpRequest.getRequestURI();
            
            // Only log SOAP requests
            if (path != null && path.startsWith("/soap")) {
                // First, clean the request body if it contains extra metadata
                HttpServletRequest cleanedRequest;
                try {
                    cleanedRequest = new SoapBodyCleaningRequestWrapper(httpRequest);
                    log.debug("Request body cleaned successfully");
                } catch (Exception e) {
                    log.error("Error cleaning request body, using original: {}", e.getMessage());
                    cleanedRequest = httpRequest;
                }
                
                // Wrap the cleaned request to allow reading body multiple times
                ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(cleanedRequest);
                ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);
                
                log.info("=== HTTP REQUEST TO SOAP ENDPOINT ===");
                log.info("Request Method: {}", wrappedRequest.getMethod());
                log.info("Request URI: {}", wrappedRequest.getRequestURI());
                log.info("Request URL: {}", wrappedRequest.getRequestURL().toString());
                log.info("Query String: {}", wrappedRequest.getQueryString());
                
                // Log all headers
                log.info("--- HTTP Headers ---");
                Enumeration<String> headerNames = wrappedRequest.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    String headerValue = wrappedRequest.getHeader(headerName);
                    log.info("  {}: {}", headerName, headerValue);
                }
                
                // Specifically log Content-Type as it's critical for SOAP
                String contentType = wrappedRequest.getContentType();
                log.info("--- CRITICAL: Content-Type = '{}' ---", contentType);
                if (contentType == null || (!contentType.contains("xml") && !contentType.contains("soap"))) {
                    log.error("*** WARNING: Content-Type is missing or incorrect! Expected: text/xml or application/soap+xml ***");
                }
                
                // Process the request first (this will cache the body)
                chain.doFilter(wrappedRequest, wrappedResponse);
                
                // Now we can safely read the cached body
                if ("POST".equalsIgnoreCase(wrappedRequest.getMethod()) || "PUT".equalsIgnoreCase(wrappedRequest.getMethod())) {
                    try {
                        byte[] contentAsByteArray = wrappedRequest.getContentAsByteArray();
                        if (contentAsByteArray.length > 0) {
                            String body = new String(contentAsByteArray, StandardCharsets.UTF_8);
                            log.info("--- Request Body ---");
                            log.info("Body Length: {} bytes", body.length());
                            log.info("Body Content: {}", body);
                            
                            // Check if body looks like XML/SOAP
                            String trimmedBody = body.trim();
                            if (trimmedBody.startsWith("<")) {
                                log.info("Body appears to be XML/SOAP");
                            } else {
                                log.warn("Body does not start with '<' - starts with: {}", 
                                        body.length() > 100 ? body.substring(0, 100) : body);
                                
                                // Try to find the SOAP envelope in the body
                                int envelopeIndex = body.indexOf("<soapenv:Envelope");
                                if (envelopeIndex > 0) {
                                    log.error("*** PROBLEM DETECTED: SOAP envelope found at position {}, but body starts with garbage! ***", envelopeIndex);
                                    log.error("Body before SOAP envelope: '{}'", body.substring(0, Math.min(envelopeIndex, 200)));
                                    log.error("This suggests the client is sending form data or metadata before the SOAP XML.");
                                    log.error("SOLUTION: In SoapUI, ensure you're sending raw XML, not form-encoded data.");
                                } else {
                                    int xmlIndex = body.indexOf("<");
                                    if (xmlIndex > 0) {
                                        log.error("*** PROBLEM DETECTED: XML found at position {}, but body starts with: '{}' ***", 
                                                xmlIndex, body.substring(0, Math.min(xmlIndex, 200)));
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error reading request body: {}", e.getMessage(), e);
                    }
                }
                
                log.info("=== HTTP RESPONSE FROM SOAP ENDPOINT ===");
                log.info("Response Status: {}", wrappedResponse.getStatus());
                log.info("=== END HTTP REQUEST/RESPONSE ===");
                
                // Copy the response back
                wrappedResponse.copyBodyToResponse();
                return;
            }
        }
        
        // For non-SOAP requests, just pass through
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info("SoapRequestLoggingFilter destroyed");
    }
}

