package org.apache.camel.example.fhir;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Resource;

public class AggregateSalesForceResponse implements AggregationStrategy {

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // the first time there are no existing message and therefore
        // the oldExchange is null. In these cases we just return
        // the newExchange
        if (oldExchange == null) {
            return newExchange;
        }

        // now we have both an existing message (oldExchange)
        // and a incoming message (newExchange)
        // we want to merge together.

        // in this example we add their bodies
        String oldBody = oldExchange.getIn().getBody(String.class);
        String newBody = newExchange.getIn().getBody(String.class);


        oldExchange.getIn().setBody(oldBody + newBody);
        // and return it
        return oldExchange;
    }

}
