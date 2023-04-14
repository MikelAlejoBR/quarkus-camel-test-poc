package org.acme;

import io.vertx.core.json.DecodeException;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MainRoutes extends EndpointRouteBuilder {

    /**
     * <p>Configures the main routes. It simply:
     * <ul>
     *     <li>Receives {@code POST} requests in the {@code /api/path} endpoint.</li>
     *     <li>Redirects them to a "two" endpoint and verifies that the JSON is valid.</li>
     *     <li>Redirects the valid payload to a third endpoint which redirects it to a Kafka topic.</li>
     * </ul>
     * </p>
     */
    @Override
    public void configure() {
        // Take care of the invalid payloads.
        onException(DecodeException.class, NullPointerException.class)
            .process(new ExceptionTransformer())
            .handled(true)
            .to(direct("failures"));

        // Entry point.
        rest("/api")
            .post("/path")
            .routeId("one")
            .to("direct:two");

        // Validate the JSON.
        from(direct("two"))
            .routeId("two")
            .process(new JsonValidatorProcessor())
            .to(direct("three"))
            .end();

        // Send it to Kafka!
        from(direct("three"))
            .routeId("three")
            .to(kafka("hello.world"))
            .end();

        // Send the failures to a different Kafka topic.
        from(direct("failures"))
            .routeId("failures")
            .to(kafka("failures"))
            .end();
    }
}
