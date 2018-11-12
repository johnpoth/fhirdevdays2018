package org.apache.camel.example.fhir;

import java.io.IOException;
import ca.uhn.hl7v2.model.v24.message.ORU_R01;
import ca.uhn.hl7v2.model.v24.segment.PID;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.component.fhir.internal.FhirConstants;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

@Converter
public class BundleConverter {

    private BundleConverter(){}

    @Converter
    public static Bundle csvToBundle(ORU_R01 msg, Exchange exchange) throws IOException {
        final PID pid = msg.getPATIENT_RESULT().getPATIENT().getPID();
        String surname = pid.getPatientName()[0].getFamilyName().getFn1_Surname().getValue();
        String name = pid.getPatientName()[0].getGivenName().getValue();
        String patientId = msg.getPATIENT_RESULT().getPATIENT().getPID().getPatientID().getCx1_ID().getValue();
        Patient patient = new Patient();
        patient.addName().addGiven(name);
        patient.getNameFirstRep().setFamily(surname);
        patient.setId(patientId);
        Bundle bundle = new Bundle().setType(Bundle.BundleType.TRANSACTION);
        bundle.addEntry().setResource(patient).getRequest().setMethod(Bundle.HTTPVerb.POST);
        exchange.getIn().setHeader(FhirConstants.PROPERTY_PREFIX + "bundle", bundle);
        return bundle;
    }
}
