package org.apache.camel.example.fhir;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import org.apache.camel.component.hl7.HL7MLLPNettyEncoderFactory;

public class Hl7Encoder {
    @Produces
    @ApplicationScoped
    @Named("hl7encoder")
    HL7MLLPNettyEncoderFactory properties() {
        return new HL7MLLPNettyEncoderFactory();
    }
}
