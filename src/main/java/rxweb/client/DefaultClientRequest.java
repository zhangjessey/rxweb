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

package rxweb.client;

import java.nio.charset.StandardCharsets;

import reactor.io.buffer.Buffer;
import reactor.rx.Stream;
import reactor.rx.Streams;
import rxweb.http.AbstractRequest;
import rxweb.http.Method;
import rxweb.http.Protocol;
import rxweb.http.RequestHeaders;

/**
 * @author Sebastien Deleuze
 */
public class DefaultClientRequest extends AbstractRequest implements ClientRequest {

	protected ClientRequestHeaders headers;
	protected Stream<Buffer> contentSource;

	@Override
	public ClientRequestHeaders getHeaders() {
		return this.headers;
	}

	@Override
	public ClientRequest uri(String uri) {
		this.uri = uri;
		return this;
	}

	@Override
	public ClientRequest protocol(Protocol protocol) {
		this.protocol = protocol;
		return this;
	}

	@Override
	public ClientRequest method(Method method) {
		this.method = method;
		return this;
	}

	@Override
	public ClientRequest header(String name, String value) {
		this.headers.set(name, value);
		return this;
	}

	@Override
	public ClientRequest addHeader(String name, String value) {
		this.headers.add(name, value);
		return null;
	}

	@Override
	public ClientRequest accept(String value) {
		this.header(RequestHeaders.ACCEPT, value);
		return this;
	}

	@Override
	public Stream<Buffer> getRawSource() {
		return this.contentSource;
	}

	@Override
	public ClientRequest rawSource(Stream<Buffer> contentSource) {
		this.contentSource = contentSource;
		return this;
	}

	@Override
	public ClientRequest stringSource(Stream<String> contentSource) {
		this.contentSource =  contentSource.map(content -> Buffer.wrap(
				content.getBytes(StandardCharsets.UTF_8)));
		return this;
	}

	@Override
	public ClientRequest rawContent(Buffer content) {
		this.contentSource = Streams.just(content);
		return this;
	}

	@Override
	public ClientRequest stringContent(String content) {
		this.contentSource = Streams.just(Buffer.wrap(content.getBytes(StandardCharsets.UTF_8)));
		return this;
	}

	@Override
	public ClientRequest content(Object content) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequest source(Stream<Object> value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
