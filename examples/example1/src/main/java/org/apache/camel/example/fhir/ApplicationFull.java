//package org.apache.camel.example.fhir;
//
//import javax.enterprise.context.ApplicationScoped;
//import javax.enterprise.inject.Produces;
//import javax.inject.Named;
//import org.apache.camel.CamelException;
//import org.apache.camel.LoggingLevel;
//import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.cdi.ContextName;
//import org.apache.camel.component.properties.PropertiesComponent;
//import org.apache.http.ProtocolException;
//import org.hl7.fhir.dstu3.model.Bundle;
//
//public class ApplicationFull {
//
//    @ContextName("camel-example-fhir-cdi")
//    static class FhirRoute extends RouteBuilder {
//
//        @Override
//        public void configure() {
//            from("file:{{input}}").routeId("fhir-csv-example")
//                .onException(CamelException.class)
//                    .handled(true)
//                    .log(LoggingLevel.ERROR, "Error unmarshalling ${file:name} ${exception.message}")
//                    .marshal().csv()
//                    .to("file:{{error.dir}}")
//                    .end()
//                .log("Converting ${file:name}")
//                .unmarshal().csv()
//                .convertBodyTo(Bundle.class)
//                .onException(ProtocolException.class)
//                    .log(LoggingLevel.ERROR, "Error connecting to FHIR server with URL:{{serverUrl}}, please check the application.properties file ${exception.message}")
//                    .maximumRedeliveries(5)
//                    .redeliveryDelay(5000)
//                    .end()
//                .to("fhir://transaction/withBundle?serverUrl={{serverUrl}}&fhirVersion={{fhirVersion}}")
//                .marshal().fhirJson(true)
//                .log("CSV imported successfully: ${body}");
//        }
//    }
//
//    @Produces
//    @ApplicationScoped
//    @Named("properties")
//    PropertiesComponent properties() {
//        return new PropertiesComponent("classpath:application.properties");
//    }
//}
