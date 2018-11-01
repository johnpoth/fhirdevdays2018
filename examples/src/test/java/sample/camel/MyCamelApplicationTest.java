package sample.camel;

import java.io.File;
import java.io.IOException;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.EnableRouteCoverage;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = MyCamelApplication.class,
    properties = "input = target/work/fhir/testinput")
@EnableRouteCoverage
@MockEndpointsAndSkip("fhir*")
public class MyCamelApplicationTest {

    @EndpointInject(uri = "mock:fhir:create/resource")
    private MockEndpoint mock;

    @Before
    public void copyData() throws IOException {
        FileUtils.copyDirectory(new File("src/main/data"), new File("target/work/fhir/testinput"));
    }

    @Test
    public void shouldPushConvertedHl7toFhir() throws Exception {
        mock.expectedBodiesReceived("{\"resourceType\":\"Patient\",\"id\":\"100005056\",\"name\":[{\"family\":\"Freeman\",\"given\":[\"Vincent\"]}]}");
        
        mock.assertIsSatisfied();
    }

}
