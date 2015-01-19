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

package rxweb.http;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Sebastien Deleuze
 */
public abstract class AbstractRequest implements Request {

	protected Protocol protocol;
	protected RequestMethod method;
	protected String uri;

	@Override
	public Protocol getProtocol() {
		return this.protocol;
	}

	@Override
	public String getUri() {
		return this.uri;
	}

	@Override
	public RequestMethod getMethod() {
		return this.method;
	}

}
