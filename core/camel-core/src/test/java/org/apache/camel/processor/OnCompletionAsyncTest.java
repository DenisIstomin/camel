/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.processor;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.ContextTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OnCompletionAsyncTest extends ContextTestSupport {

    @Override
    public boolean isUseRouteBuilder() {
        return false;
    }

    @Test
    public void testAsyncComplete() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                onCompletion().parallelProcessing().to("mock:before").delay(250).setBody(simple("OnComplete:${body}"))
                        .to("mock:after");

                from("direct:start").process(new MyProcessor()).to("mock:result");
            }
        });
        context.start();

        getMockEndpoint("mock:before").expectedBodiesReceived("Bye World");
        getMockEndpoint("mock:before").expectedPropertyReceived(Exchange.ON_COMPLETION, true);
        getMockEndpoint("mock:after").expectedBodiesReceived("OnComplete:Bye World");

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived("Bye World");

        String out = template.requestBody("direct:start", "Hello World", String.class);
        assertEquals("Bye World", out);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testAsyncFailure() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                onCompletion().parallelProcessing().to("mock:before").delay(250).setBody(simple("OnComplete:${body}"))
                        .to("mock:after");

                from("direct:start").process(new MyProcessor()).to("mock:result");
            }
        });
        context.start();

        getMockEndpoint("mock:before").expectedBodiesReceived("Kaboom");
        getMockEndpoint("mock:before").expectedPropertyReceived(Exchange.ON_COMPLETION, true);
        getMockEndpoint("mock:after").expectedBodiesReceived("OnComplete:Kaboom");

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(0);

        try {
            template.requestBody("direct:start", "Kaboom");
            fail("Should throw exception");
        } catch (CamelExecutionException e) {
            assertEquals("Kaboom", e.getCause().getMessage());
        }

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testAsyncCompleteUseOriginalBody() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                onCompletion().useOriginalBody().parallelProcessing().to("mock:before").delay(250)
                        .setBody(simple("OnComplete:${body}")).to("mock:after");

                from("direct:start").process(new MyProcessor()).to("mock:result");
            }
        });
        context.start();

        getMockEndpoint("mock:before").expectedBodiesReceived("Hello World");
        getMockEndpoint("mock:before").expectedPropertyReceived(Exchange.ON_COMPLETION, true);
        getMockEndpoint("mock:after").expectedBodiesReceived("OnComplete:Hello World");

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived("Bye World");

        String out = template.requestBody("direct:start", "Hello World", String.class);
        assertEquals("Bye World", out);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testAsyncFailureUseOriginalBody() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                onCompletion().useOriginalBody().parallelProcessing().to("mock:before").delay(250)
                        .setBody(simple("OnComplete:${body}")).to("mock:after");

                from("direct:start").transform(body().prepend("Before:${body}")).process(new MyProcessor()).to("mock:result");
            }
        });
        context.start();

        getMockEndpoint("mock:before").expectedBodiesReceived("Kaboom");
        getMockEndpoint("mock:before").expectedPropertyReceived(Exchange.ON_COMPLETION, true);
        getMockEndpoint("mock:after").expectedBodiesReceived("OnComplete:Kaboom");

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(0);

        try {
            template.requestBody("direct:start", "Kaboom");
            fail("Should throw exception");
        } catch (CamelExecutionException e) {
            assertEquals("Kaboom", e.getCause().getMessage());
        }

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testAsyncCompleteOnCompleteFail() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                onCompletion().parallelProcessing().to("mock:before").delay(250).setBody(simple("OnComplete:${body}"))
                        // this exception does not cause any side effect as we are
                        // in async mode
                        .throwException(new IllegalAccessException("From onComplete")).to("mock:after");

                from("direct:start").process(new MyProcessor()).to("mock:result");
            }
        });
        context.start();

        getMockEndpoint("mock:before").expectedBodiesReceived("Bye World");
        getMockEndpoint("mock:before").expectedPropertyReceived(Exchange.ON_COMPLETION, true);
        getMockEndpoint("mock:after").expectedMessageCount(0);

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived("Bye World");

        String out = template.requestBody("direct:start", "Hello World", String.class);
        assertEquals("Bye World", out);

        assertMockEndpointsSatisfied();
    }

    public static class MyProcessor implements Processor {

        public MyProcessor() {
        }

        @Override
        public void process(Exchange exchange) {
            if (exchange.getIn().getBody(String.class).contains("Kaboom")) {
                throw new IllegalArgumentException("Kaboom");
            }
            exchange.getIn().setBody("Bye World");
        }
    }

}
