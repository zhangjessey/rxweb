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

import rxweb.http.Method;
import rxweb.http.Request;

/**
 * @author Sebastien Deleuze
 */
public class MappingCondition implements Condition<Request> {

	private final MethodCondition methodCondition;
	private final PathCondition pathCondition;

	public MappingCondition(Builder builder) {
		this.methodCondition = builder.methodCondition;
		this.pathCondition = builder.pathCondition;
	}

	@Override
	public boolean match(Request request) {
		return this.methodCondition.match(request) && this.pathCondition.match(request);
	}

	public static class Builder {

		private MethodCondition methodCondition;
		private PathCondition pathCondition;

		public static Builder from(String path) {
			Builder builder = new Builder();
			builder.pathCondition = new PathCondition(path);
			return builder;
		}

		public static Builder from(PathCondition pathCondition) {
			Builder builder = new Builder();
			builder.pathCondition = pathCondition;
			return builder;
		}

		public Builder method(Method method) {
			this.methodCondition = new MethodCondition(method);
			return this;
		}

		public Builder method(Method... methods) {
			this.methodCondition = new MethodCondition(methods);
			return this;
		}

		public MappingCondition build() {
			if(this.methodCondition.getMethods().isEmpty()) {
				this.methodCondition = new MethodCondition(Method.GET);
			}
			return new MappingCondition(this);
		}

	}
}
