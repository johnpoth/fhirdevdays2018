package org.apache.camel.example.fhir;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.component.fhir.internal.FhirConstants;

public class Application {

    @ContextName("camel-example-fhir-cdi")
    static class FhirRoute extends RouteBuilder {

        @Override
        public void configure() {


            restConfiguration().component("jetty")
                               .port("{{http.port}}")
                               .host("{{http.host}}")
                               .apiProperty("api.title", "Patient Salesforces update")
                               .apiContextPath("api-doc");

            rest("updatePatients")
                .get("{MRNs}")
                .produces("application/json")
                .to("direct:synchronizeSalesforce");

            from("direct:synchronizeSalesforce")
                 .split(header("MRNs").tokenize(","), new AggregateSalesForceResponse())
                                            .parallelProcessing()
                                            .stopOnException()
                     .setHeader(FhirConstants.PROPERTY_PREFIX + "resourceClass", simple("Patient"))
                     .setHeader(FhirConstants.PROPERTY_PREFIX + "stringId", simple("${body}"))
                     .to("fhir://read/resourceById?serverUrl={{serverUrl}}&fhirVersion={{fhirVersion}}")
                     .to("direct:convert-patient-to-ehr")
                     // create or update Salesforce Health Cloud EHR
                     .to("salesforce:upsertSObject?sObjectIdName=HealthCloudGA__SourceSystemId__c&sObjectName=HealthCloudGA__EhrPatient__c&sObjectIdValue=${body.HealthCloudGA__SourceSystemId__c}")
                     .end();



            from("direct:convert-patient-to-account")
                  .log("${body}");

            from("direct:salesforce")
                    .setBody(simple("TestX"))
                    .log("${body}");

        }
    }

}
