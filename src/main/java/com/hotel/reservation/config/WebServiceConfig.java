package com.hotel.reservation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Slf4j
@Configuration
@EnableWs
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        log.info("Configuring MessageDispatcherServlet for SOAP endpoint");
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        log.info("MessageDispatcherServlet configured for path: /soap/*");
        return new ServletRegistrationBean<>(servlet, "/soap/*");
    }

    @Bean
    public SaajSoapMessageFactory messageFactory() {
        log.info("Configuring SaajSoapMessageFactory with SOAP 1.1");
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.setSoapVersion(SoapVersion.SOAP_11);
        log.info("SaajSoapMessageFactory configured successfully");
        return messageFactory;
    }

    @Override
    public void addInterceptors(java.util.List<EndpointInterceptor> interceptors) {
        log.info("Adding SOAP logging interceptor");
        interceptors.add(new SoapLoggingInterceptor());
        log.info("SOAP logging interceptor added successfully");
    }

    @Bean
    public SoapExceptionHandler exceptionResolver() {
        log.info("Configuring SOAP exception handler");
        SoapExceptionHandler exceptionResolver = new SoapExceptionHandler();
        log.info("SOAP exception handler configured");
        return exceptionResolver;
    }

    @Bean(name = "reservations")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema reservationSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ReservationPort");
        wsdl11Definition.setLocationUri("/soap");
        wsdl11Definition.setTargetNamespace("http://hotel.com/reservation/soap");
        wsdl11Definition.setSchema(reservationSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema reservationSchema() {
        return new SimpleXsdSchema(new ClassPathResource("reservation.xsd"));
    }
}

