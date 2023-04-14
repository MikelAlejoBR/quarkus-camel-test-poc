package org.acme;

import io.vertx.core.json.DecodeException;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class ExceptionTransformer implements Processor {
    /**
     * Transforms the incoming exceptions into user-friendly messages.
     * @param exchange the incoming exchange.
     * @throws Exception if any unexpected error occurs.
     */
    @Override
    public void process(final Exchange exchange) throws Exception {
        final Message message = exchange.getIn();

        final Throwable cause = exchange
            .getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);

        if (cause instanceof DecodeException) {
            message.setBody("{\"error\": \"The provided body contains invalid JSON!\"}");
        } else {
            message.setBody("{\"error\": \"The body of the payload cannot be empty!\"}");
        }
    }
}
