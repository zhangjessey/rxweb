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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import reactor.fn.tuple.Tuple;
import reactor.fn.tuple.Tuple2;
import reactor.io.buffer.Buffer;
import rxweb.http.Request;
import rxweb.mapping.Condition;
import rxweb.mapping.HandlerResolver;

/**
 * @author Sebastien Deleuze
 */
public class DefaultHandlerResolver implements HandlerResolver {

	private final Map<Condition<Request>, Tuple2<ServerHandler, Class<?>>> handlers = new LinkedHashMap<>();

	@Override
	public void addHandler(final Condition<Request> condition, final ServerHandler handler) {
		handlers.put(condition, Tuple.of(handler, Buffer.class));
	}

	@Override
	public <T> void addHandler(final Condition<Request> condition, final Class<T> type, final ServerHandler handler) {
		handlers.put(condition, Tuple.of(handler, type));
	}

	@Override
	public List<Tuple2<ServerHandler, Class<?>>> resolve(Request request) {
		List<Tuple2<ServerHandler, Class<?>>> requestHandlers = new ArrayList<>();
		for(Map.Entry<Condition<Request>, Tuple2<ServerHandler, Class<?>>> entry : this.handlers.entrySet()) {
			if(entry.getKey().match(request)) {
				requestHandlers.add(entry.getValue());
			}
		}
		return requestHandlers;
	}
}
