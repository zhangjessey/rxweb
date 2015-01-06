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

package rxweb.netty.server;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpVersion;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;

import org.reactivestreams.Publisher;
import reactor.io.buffer.Buffer;
import reactor.rx.Stream;
import reactor.rx.Streams;
import rx.RxReactiveStreams;
import rxweb.http.Method;
import rxweb.http.Protocol;
import rxweb.server.ServerRequest;
import rxweb.server.ServerRequestHeaders;

/**
 * @author Sebastien Deleuze
 */
public class NettyServerRequestAdapter implements ServerRequest {

	private final HttpServerRequest<ByteBuf> nettyRequest;
	private final ServerRequestHeaders headers;

	public NettyServerRequestAdapter(HttpServerRequest<ByteBuf> request) {
		this.nettyRequest = request;
		this.headers = new NettyRequestHeadersAdapter(this.nettyRequest.getHeaders());
	}

	@Override
	public Protocol getProtocol() {
		HttpVersion version = this.nettyRequest.getHttpVersion();
		if (version.equals(HttpVersion.HTTP_1_0)) {
			return Protocol.HTTP_1_0;
		} else if (version.equals(HttpVersion.HTTP_1_1)) {
			return Protocol.HTTP_2_0;
		}
		throw new IllegalStateException(version.protocolName() + " not supported");
	}

	@Override
	public String getUri() {
		return this.nettyRequest.getUri();
	}

	@Override
	public Method getMethod() {
		return new Method(this.nettyRequest.getHttpMethod().name());
	}

	@Override
	public ServerRequestHeaders getHeaders() {
		return this.headers;
	}

	@Override
	public Stream<Buffer> getRawContent() {
		Publisher<Buffer> publisher = RxReactiveStreams.toPublisher(this.nettyRequest.getContent().map(byteBuf -> Buffer.wrap(byteBuf.array())));
		return Streams.create(publisher);
	}

	@Override
	public <T> Stream<T> getContent(Class<T> clazz) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Stream<String> getStringContent() {
		Publisher<String> publisher = RxReactiveStreams.toPublisher(this.nettyRequest.getContent().map((content) -> content.toString(StandardCharsets.UTF_8)));
		return Streams.create(publisher);
	}

}
