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

package rxweb.netty.client;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import rxweb.client.ClientRequest;
import rxweb.client.ClientRequestHeaders;
import rxweb.http.Method;
import rxweb.http.Protocol;
import rxweb.http.RequestHeaders;

/**
 * @author Sebastien Deleuze
 */
public class NettyClientRequestAdapter implements ClientRequest {

	private final HttpClientRequest<ByteBuf> nettyRequest;

	public NettyClientRequestAdapter(HttpClientRequest<ByteBuf> nettyRequest) {
		this.nettyRequest = nettyRequest;
	}

	@Override
	public Protocol getProtocol() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public String getUri() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Method getMethod() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHeaders getHeaders() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequest uri(String uri) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequest protocol(Protocol protocol) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequest method(Method method) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequest header(String name, String value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequest addHeader(String name, String value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequest accept(String value) {
		this.header(RequestHeaders.ACCEPT, value);
		return this;
	}

	@Override
	public ClientRequest write(ByteBuf content) {
		return null;
	}

	@Override
	public ClientRequest write(Object content) {
		return null;
	}

	@Override
	public ClientRequest writeString(String content) {
		return null;
	}

}
