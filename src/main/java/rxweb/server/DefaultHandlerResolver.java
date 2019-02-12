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

import rxweb.http.Request;
import rxweb.mapping.Condition;
import rxweb.mapping.HandlerResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Sebastien Deleuze
 * @author zhangjessey
 */
public class DefaultHandlerResolver implements HandlerResolver {

	@Override
	public void addHandler(final Condition<Request> condition, final ServerHandler handler) {
		Init.handlers.put(condition, handler);
	}

	@Override
	public List<ServerHandler> resolve(Request request) {
		List<ServerHandler> requestHandlers = new ArrayList<>();
		for (Map.Entry<Condition<Request>, ServerHandler> entry : Init.handlers.entrySet()) {
			if(entry.getKey().match(request)) {
				requestHandlers.add(entry.getValue());
			}
		}
		return requestHandlers;
	}
}
