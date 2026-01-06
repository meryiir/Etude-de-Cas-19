package com.hotel.reservation.config;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SoapBodyCleaningRequestWrapper extends HttpServletRequestWrapper {
    
    private final byte[] cleanedBody;
    private final Map<String, String> modifiedHeaders = new HashMap<>();
    
    public SoapBodyCleaningRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.cleanedBody = cleanRequestBody(request);
    }
    
    private byte[] cleanRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line).append("\n");
            }
        }
        
        String bodyStr = body.toString();
        log.debug("Original body length: {} bytes", bodyStr.length());
        
        // Check if body already starts with XML
        String trimmed = bodyStr.trim();
        if (trimmed.startsWith("<")) {
            log.debug("Body already starts with XML, no cleaning needed");
            return bodyStr.getBytes(StandardCharsets.UTF_8);
        }
        
        // Find the first XML tag (could be <soapenv:Envelope, <soap:Envelope, <Envelope, or just <)
        int xmlStart = -1;
        
        // Try different SOAP envelope patterns
        String[] patterns = {
            "<soapenv:Envelope",
            "<soap:Envelope",
            "<SOAP-ENV:Envelope",
            "<SOAP:Envelope",
            "<Envelope",
            "<"
        };
        
        for (String pattern : patterns) {
            xmlStart = bodyStr.indexOf(pattern);
            if (xmlStart >= 0) {
                log.info("Found XML starting pattern '{}' at position {}", pattern, xmlStart);
                break;
            }
        }
        
        if (xmlStart < 0) {
            log.warn("No XML found in request body, returning original");
            return bodyStr.getBytes(StandardCharsets.UTF_8);
        }
        
        // Extract XML from the found position
        String cleaned = bodyStr.substring(xmlStart);
        log.info("Cleaned body: removed {} bytes of metadata, {} bytes of XML remaining", 
                xmlStart, cleaned.length());
        log.debug("Cleaned body preview: {}", cleaned.substring(0, Math.min(200, cleaned.length())));
        
        // Update Content-Type header to text/xml since body was cleaned
        modifiedHeaders.put("Content-Type", "text/xml; charset=UTF-8");
        
        return cleaned.getBytes(StandardCharsets.UTF_8);
    }
    
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            private final ByteArrayInputStream bis = new ByteArrayInputStream(cleanedBody);
            
            @Override
            public boolean isFinished() {
                return bis.available() == 0;
            }
            
            @Override
            public boolean isReady() {
                return true;
            }
            
            @Override
            public void setReadListener(ReadListener listener) {
                // Not needed for synchronous reading
            }
            
            @Override
            public int read() throws IOException {
                return bis.read();
            }
            
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return bis.read(b, off, len);
            }
        };
    }
    
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }
    
    @Override
    public int getContentLength() {
        return cleanedBody.length;
    }
    
    @Override
    public long getContentLengthLong() {
        return cleanedBody.length;
    }
    
    @Override
    public String getHeader(String name) {
        // Return modified header if present, otherwise delegate to parent
        if (modifiedHeaders.containsKey(name)) {
            return modifiedHeaders.get(name);
        }
        return super.getHeader(name);
    }
    
    @Override
    public Enumeration<String> getHeaderNames() {
        // Combine original headers with modified ones
        Map<String, String> allHeaders = new HashMap<>();
        Enumeration<String> originalHeaders = super.getHeaderNames();
        while (originalHeaders.hasMoreElements()) {
            String headerName = originalHeaders.nextElement();
            allHeaders.put(headerName, super.getHeader(headerName));
        }
        allHeaders.putAll(modifiedHeaders);
        return Collections.enumeration(allHeaders.keySet());
    }
}

