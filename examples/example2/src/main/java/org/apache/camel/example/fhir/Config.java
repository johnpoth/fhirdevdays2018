package org.apache.camel.example.fhir;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import org.apache.camel.component.properties.PropertiesComponent;

public class Config {

    @Produces
    @ApplicationScoped
    @Named("properties")
    PropertiesComponent properties() {
        return new PropertiesComponent("classpath:application.properties");
    }

}
