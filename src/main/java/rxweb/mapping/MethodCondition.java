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

package rxweb.mapping;

import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sebastien Deleuze
 * @author zhangjessey
 */
public class MethodCondition implements Condition<HttpServerRequest> {

	private final Set<HttpMethod> methods = new HashSet<>();

	public MethodCondition(HttpMethod method) {
		this.methods.add(method);
	}

	public MethodCondition(HttpMethod... methods) {
		Collections.addAll(this.methods, methods);
	}

	public Set<HttpMethod> getMethods() {
		return methods;
	}

	@Override
	public boolean match(HttpServerRequest request) {
		return this.methods.contains(request.getHttpMethod());
	}
}
