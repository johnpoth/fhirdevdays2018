package org.apache.camel.example.fhir;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.component.fhir.internal.FhirConstants;

public class Application {

    @ContextName("camel-example-fhir-cdi")
    static class FhirRoute extends RouteBuilder {

        @Override
        public void configure() {
            restConfiguration().component("undertow")
                               .port("{{http.port}}")
                               .host("{{http.host}}")
                               .apiProperty("api.title", "Patient Observation example")
                               .apiContextPath("api-doc");

            rest("getPatientObservation")
                .get("{MRN}")
                .produces("text/plain")
                .to("direct:getFhirPatientObservation");

            from("direct:getFhirPatientObservation")
                 .setHeader(FhirConstants.PROPERTY_PREFIX + "resourceClass", simple("Patient"))
                 .setHeader(FhirConstants.PROPERTY_PREFIX + "stringId", simple("${header.MRN}"))
                 .setHeader(FhirConstants.PROPERTY_PREFIX + "url",
                         simple("Observation?patient._id=${header.MRN}"))
                 .multicast(new PatientObservationToBundleAggregationStrategy())
                   .parallelProcessing()
                   .to("fhir://read/resourceById?serverUrl={{serverUrl}}&fhirVersion={{fhirVersion}}",
                       "fhir://search/searchByUrl?serverUrl={{serverUrl}}&fhirVersion={{fhirVersion}}")
                   .end()
                 .to("direct:send-to-hl7-server");

            from("direct:send-to-hl7-server")
                  .to("direct:convert-fhir-bundle-hl7-message")
                  .marshal().hl7()
                  .to("netty4:tcp://{{hl7.host}}:{{hl7.port}}?sync=true&decoder=#hl7decoder&encoder=#hl7encoder");
        }
    }

}
