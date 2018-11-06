package org.apache.camel.example.fhir;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import org.apache.camel.component.hl7.HL7MLLPNettyDecoderFactory;

public class Hl7Decoder {

    @Produces
    @ApplicationScoped
    @Named("hl7decoder")
    HL7MLLPNettyDecoderFactory properties() {
        return new HL7MLLPNettyDecoderFactory();
    }

}
