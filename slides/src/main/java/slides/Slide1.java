package slides;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class Slide1 {


    public void simpleExample() throws Exception {



        CamelContext camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://orders/?")
                        .convertBodyTo(String.class)
                        .to("jms:orders.queue");
            }
        });



    }
}
