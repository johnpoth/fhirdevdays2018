package org.apache.camel.example.fhir;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.http.ProtocolException;
import org.hl7.fhir.dstu3.model.Bundle;

public class Application {

    @ContextName("camel-example-fhir-cdi")
    static class FhirRoute extends RouteBuilder {

        @Override
        public void configure() {
            from("file:{{input}}").routeId("fhir-example")
                .onException(ProtocolException.class)
                    .handled(true)
                    .log(LoggingLevel.ERROR, "Error connecting to FHIR server with URL:{{serverUrl}}, please check the application.properties file ${exception.message}")
                    .end()
                .onException(Exception.class)
                    .handled(true)
                    .log(LoggingLevel.ERROR, "Error importing ${file:name} ${exception.message}")
                    .stop()
                    .end()
                .log("Converting ${file:name}")
                // unmarshall file to hl7 message
                .unmarshal().hl7()
                // very simple mapping from a HL7 V2 message to dstu3 bundle
                .convertBodyTo(Bundle.class)
                // create Bundle in our FHIR server
                .to("fhir://transaction/withBundle?serverUrl={{serverUrl}}&fhirVersion={{fhirVersion}}");
        }
    }

    @Produces
    @ApplicationScoped
    @Named("properties")
    PropertiesComponent properties() {
        return new PropertiesComponent("classpath:application.properties");
    }

}
