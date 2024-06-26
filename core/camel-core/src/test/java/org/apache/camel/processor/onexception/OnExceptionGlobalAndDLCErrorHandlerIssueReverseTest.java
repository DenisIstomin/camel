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
package org.apache.camel.processor.onexception;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;

public class OnExceptionGlobalAndDLCErrorHandlerIssueReverseTest extends ContextTestSupport {

    @Test
    public void testNoOnGlobalException() throws Exception {
        getMockEndpoint("mock:foo").expectedMessageCount(1);
        getMockEndpoint("mock:dead").expectedMessageCount(0);
        getMockEndpoint("mock:global").expectedMessageCount(1);
        getMockEndpoint("mock:local").expectedMessageCount(0);

        template.sendBody("direct:foo", "Hello World");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOnRouteException() throws Exception {
        getMockEndpoint("mock:bar").expectedMessageCount(1);
        getMockEndpoint("mock:dead").expectedMessageCount(0);
        getMockEndpoint("mock:global").expectedMessageCount(0);
        getMockEndpoint("mock:local").expectedMessageCount(1);

        template.sendBody("direct:bar", "Hello World");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                errorHandler(deadLetterChannel("mock:dead"));

                onException(Exception.class).handled(true).to("mock:global");

                from("direct:foo").routeId("foo").to("mock:foo").throwException(new IllegalArgumentException("Damn"));

                from("direct:bar").routeId("bar").onException(IllegalArgumentException.class).handled(true).to("mock:local")
                        .end().to("mock:bar")
                        .throwException(new IllegalArgumentException("Damn"));
            }
        };
    }
}
