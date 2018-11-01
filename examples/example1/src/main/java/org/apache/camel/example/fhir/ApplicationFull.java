/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.example.fhir;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import org.apache.camel.CamelException;
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
            from("file:{{input}}").routeId("fhir-csv-example")
                .onException(CamelException.class)
                    .handled(true)
                    .log(LoggingLevel.ERROR, "Error unmarshalling ${file:name} ${exception.message}")
                    .marshal().csv()
                    .to("file:{{error.dir}}")
                    .end()
                .log("Converting ${file:name}")
                .unmarshal().csv()
                .convertBodyTo(Bundle.class)
                .onException(ProtocolException.class)
                    .log(LoggingLevel.ERROR, "Error connecting to FHIR server with URL:{{serverUrl}}, please check the application.properties file ${exception.message}")
                    .maximumRedeliveries(5)
                    .redeliveryDelay(5000)
                    .end()
                .to("fhir://transaction/withBundle?serverUrl={{serverUrl}}&fhirVersion={{fhirVersion}}")
                .marshal().fhirJson(true)
                .log("CSV imported successfully: ${body}");
        }
    }

    @Produces
    @ApplicationScoped
    @Named("properties")
    PropertiesComponent properties() {
        return new PropertiesComponent("classpath:application.properties");
    }
}
