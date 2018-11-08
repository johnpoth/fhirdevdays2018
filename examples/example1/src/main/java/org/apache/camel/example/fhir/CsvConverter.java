package org.apache.camel.example.fhir;

import java.io.IOException;
import java.util.List;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.component.fhir.internal.FhirConstants;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

@Converter
public class CsvConverter {

    private CsvConverter(){}

    @Converter
    public static Bundle csvToBundle(List<List<String>> patients, Exchange exchange) throws IOException {
        Bundle bundle = new Bundle().setType(Bundle.BundleType.TRANSACTION);
        for (List<String> patient: patients) {
            Patient fhirPatient = new Patient();
            fhirPatient.setId(patient.get(0));
            fhirPatient.addName().addGiven(patient.get(1));
            fhirPatient.getNameFirstRep().setFamily(patient.get(2));
            bundle.addEntry().setResource(fhirPatient).getRequest().setMethod(Bundle.HTTPVerb.POST);
        }
        exchange.getIn().setHeader(FhirConstants.PROPERTY_PREFIX + "bundle", bundle);
        return bundle;
    }
}
