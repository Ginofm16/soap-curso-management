package com.gn.soap.webservices.soapcursomanagement.soap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

//Enable SOAP Web Service features in this Spring Boot application.
@EnableWs
//This class contains Apring configuration.
@Configuration
public class WebServiceConfig {
	/*cuando una solicitud llegue a Spring sera procesado primero por un servlet dispatcher y luego
	 * identifica que controlador manejaria la solicitud.*/
	/*cuando creamos un mensaje dispatcher servlet necesitamos pasarle un par de inputs, el primero
	* es un application context y lo otro es el url*/
	//- MessageDispatcherServlet
		//AplicationContext
	//- url -> /ws/*
	
	@Bean
	//ayud asignar el servlet a la uri
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext context) {
		MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
		messageDispatcherServlet.setApplicationContext(context);
		messageDispatcherServlet.setTransformWsdlLocations(true);
		
		return new ServletRegistrationBean(messageDispatcherServlet,"/ws/*");
	}

	//Spring crea el WSDL automaticamente, no lo crearemos a mano
	// /ws/courses.wsdl
	// course-details.xsd
	
	/*se define un esquema Wsdl.
	 * The name of the bean is the name of the wsdl in the URL.*/
	@Bean(name="courses")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema coursesSchema) {
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();	
		//PortType - CoursePort
		definition.setPortTypeName("CoursePort");
		//Default namespace - http://gn.com/courses
		definition.setTargetNamespace("http://gn.com/courses");
		//The url where we want to expose the wsdl at
		definition.setLocationUri("/ws");
		//We would create WSDL based on the xsd defined.....
		definition.setSchema(coursesSchema);

		return definition;
	}
	
	
	/*lo primero que hace spring es crear una instancia de este Bean y lo coloca o conecta automaticamente en
	 *defaultWsdl11Definition() */
	/*comencemos definiendo el esquema, la forma en el que definiriamos un esquema es definiendo otro
	 bean, y mediante el uso de XsdSchema*/
	@Bean
	public XsdSchema coursesSchema() {
		//......here.
		return new SimpleXsdSchema(new ClassPathResource("course-details.xsd"));
	}
	
	
	
}
