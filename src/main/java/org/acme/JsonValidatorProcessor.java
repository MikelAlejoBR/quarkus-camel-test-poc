package org.acme;

import io.vertx.core.json.JsonObject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 * A dummy processor just to validate that the incoming payload is a valid JSON
 * payload.
 */
public class JsonValidatorProcessor implements Processor {
    @Override
    public void process(final Exchange exchange) {
        final Message in = exchange.getIn();

        new JsonObject(in.getBody(String.class));
    }
}
