package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

@QuarkusTest
@TestProfile(SimplePocTest.class)
public class SimplePocTest extends CamelQuarkusTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Test
    void testTwo() throws Exception {
        AdviceWith.adviceWith(this.context, "two", a -> {
            a.weaveByToString(".*three.*").replace().to("mock:return");
        });
        AdviceWith.adviceWith(this.context, "three", a -> a.mockEndpoints("kafka:*"));

        final MockEndpoint three = getMockEndpoint("mock:return");
        three.expectedMessageCount(1);

        final Exchange exchange = createExchangeWithBody("{\"hello\": \"world\"}");
        this.template.send("direct:two", exchange);

        MockEndpoint.assertIsSatisfied(1000, TimeUnit.MILLISECONDS, three);
    }

    @Test
    void testTwoAgain() throws Exception {
        AdviceWith.adviceWith(this.context, "two", a -> {
            a.weaveByToString(".*three.*").replace().to("mock:return");
        });
        AdviceWith.adviceWith(this.context, "three", a -> a.mockEndpoints("kafka:*"));

        final MockEndpoint three = getMockEndpoint("mock:return");
        three.expectedMessageCount(1);

        final Exchange exchange = createExchangeWithBody("{\"hello\": \"world\"}");
        this.template.send("direct:two", exchange);

        MockEndpoint.assertIsSatisfied(1000, TimeUnit.MILLISECONDS, three);
    }
}
