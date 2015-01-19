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

package rxweb.engine.server.netty;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;

import reactor.io.buffer.Buffer;
import reactor.rx.Stream;
import rxweb.converter.Converter;
import rxweb.converter.ConverterResolver;
import rxweb.http.Protocol;
import rxweb.server.ServerRequest;

import java.nio.ByteBuffer;

;

/**
 * @author Sebastien Deleuze
 */
public class NettyServerRequestAdapter extends Stream<ByteBuffer> implements ServerRequest {

	private final HttpRequest nettyRequest;
	private final HttpHeaders headers;
	private ConverterResolver converterResolver;
	private Stream<Buffer> contentStream;

	public NettyServerRequestAdapter(HttpRequest request, Stream<Buffer> contentStream) {
		this.nettyRequest = request;
		this.headers = new NettyRequestHeadersAdapter(request);
		this.contentStream = contentStream;
	}

	@Override
	public void subscribe(Subscriber<? super ByteBuffer> subscriber) {
		getContentStream().subscribe(subscriber);
	}

	@Override
	public Protocol getProtocol() {
		HttpVersion version = this.nettyRequest.getProtocolVersion();
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
	public RequestMethod getMethod() {
		return RequestMethod.valueOf(this.nettyRequest.getMethod().name());
	}

	@Override
	public HttpHeaders getHeaders() {
		return this.headers;
	}

	@Override
	public void setConverterResolver(ConverterResolver converterResolver) {
		this.converterResolver = converterResolver;
	}

	@Override
	public Publisher<ByteBuffer> getContentStream() {
		return this.contentStream.map(b -> b.byteBuffer());
	}

	@Override
	public <T> Stream<T> getContentStream(Class<T> clazz) {
		Assert.state(this.converterResolver != null);
		return this.map(buffer -> this.convert(clazz, new Buffer(buffer)));
	}

	private <T> T convert(Class<T> clazz, Buffer buffer) {
		// TODO: handle media type
		Converter<Object> converter = this.converterResolver.resolveReader(clazz, null);
		if(converter == null) {
			throw new IllegalStateException("No relevant converter found.");
		}
		return converter.read(clazz, buffer);
	}

	@Override
	public Publisher<ByteBuffer> getContent() {
		return getContentInternal().map(Buffer::byteBuffer);
	}

	@Override
	public <T> Publisher<T> getContent(Class<T> clazz) {
		Assert.state(this.converterResolver != null);
		return getContentInternal().map(buffer -> {
			T value = this.convert(clazz, buffer);
			return value;
		});
	}

	private Stream<Buffer> getContentInternal() {
		return buffer().map(bufferList -> {
			Buffer buffer = new Buffer();
			bufferList.stream().forEach(buffer::append);
			buffer.flip();
			return buffer;
		});
	}

}
