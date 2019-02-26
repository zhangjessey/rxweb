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

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import rxweb.bean.WebRequest;
import rxweb.mapping.Condition;
import rxweb.mapping.HandlerResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * 默认的HandlerResolver
 *
 * @author Sebastien Deleuze
 * @author zhangjessey
 */
public class DefaultHandlerResolver implements HandlerResolver {

	private static DefaultHandlerResolver DEFAULT_HANDLER_RESOLVER = new DefaultHandlerResolver();

	public static DefaultHandlerResolver getSingleton() {
		return DEFAULT_HANDLER_RESOLVER;
	}

	@Override
	public HandlerResolver addHandler(final Condition<HttpServerRequest> condition, final WebRequestHandler<ByteBuf, ByteBuf> handler) {
		BootstrapConfig.CONDITION_REQUEST_HANDLER_MAP.put(condition, handler);
		return this;
	}

	@Override
	public List<WebRequestHandler<ByteBuf, ByteBuf>> resolve(WebRequest webRequest) {

		Matcher matcher = webRequest.getRequestPathMatcher();
		List<WebRequestHandler<ByteBuf, ByteBuf>> requestHandlers = new ArrayList<>();
		if (matcher != null && matcher.matches()) {
			WebRequestHandler<ByteBuf, ByteBuf> value = webRequest.getWebRequestHandler();
			requestHandlers.add(value);
		}
		return requestHandlers;
	}

}
