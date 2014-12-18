/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rxweb.server;

import rxweb.Server;
import rxweb.http.Method;
import rxweb.http.Request;
import rxweb.mapping.Condition;
import rxweb.mapping.HandlerResolver;
import rxweb.mapping.MappingCondition;

/**
 * @author Sebastien Deleuze
 */
public abstract class AbstractServer implements Server {

	protected String host = "0.0.0.0";
	protected int port = 8080;
	protected HandlerResolver handlerResolver = new DefaultHandlerResolver();

	public void setHandlerResolver(HandlerResolver handlerResolver) {
		this.handlerResolver = handlerResolver;
	}

	@Override
	public void addHandler(final Condition<Request> condition, final ServerHandler handler) {
		this.handlerResolver.addHandler(condition, handler);
	}

	@Override
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void get(final String path, final ServerHandler handler) {
		addHandler(MappingCondition.Builder.from(path).method(Method.GET).build(), handler);
	}

	@Override
	public void post(final String path, final ServerHandler handler) {
		addHandler(MappingCondition.Builder.from(path).method(Method.POST).build(), handler);
	}

	@Override
	public void put(final String path, final ServerHandler handler) {
		addHandler(MappingCondition.Builder.from(path).method(Method.PUT).build(), handler);
	}

	@Override
	public void delete(final String path, final ServerHandler handler) {
		addHandler(MappingCondition.Builder.from(path).method(Method.DELETE).build(), handler);
	}
}
