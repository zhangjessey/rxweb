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

package rxweb.rx.reactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.io.buffer.Buffer;
import reactor.rx.Promise;
import reactor.rx.Stream;
import reactor.rx.Streams;
import rxweb.converter.ConverterResolver;
import rxweb.http.Protocol;
import rxweb.server.ServerRequest;

import java.nio.ByteBuffer;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Sebastien Deleuze
 */
public class ReactorServerRequestAdapter implements ReactorServerRequest {

	private ServerRequest serverRequest;

	public ReactorServerRequestAdapter(ServerRequest serverRequest) {
		this.serverRequest = serverRequest;
	}

	@Override
	public HttpHeaders getHeaders() {
		return this.serverRequest.getHeaders();
	}

	@Override
	public Promise<Buffer> getContent() {
		return Streams.create(this.serverRequest.getContent()).map(Buffer::new).next();
	}

	@Override
	public <T> Promise<T> getContent(Class<T> clazz) {
		return Streams.create(this.serverRequest.getContent(clazz)).next();
	}

	@Override
	public Stream<Buffer> getContentStream() {
		return Streams.create(this.serverRequest.getContentStream()).map(Buffer::new);
	}

	@Override
	public <T> Stream<T> getContentStream(Class<T> clazz) {
		return Streams.create(this.serverRequest.getContentStream(clazz));
	}

	@Override
	public void setConverterResolver(ConverterResolver converterResolver) {
		this.serverRequest.setConverterResolver(converterResolver);
	}

	@Override
	public void subscribe(Subscriber<? super Buffer> s) {
		this.serverRequest.subscribe(new Subscriber<ByteBuffer>() {
			@Override
			public void onSubscribe(Subscription subscription) {
				s.onSubscribe(subscription);
			}

			@Override
			public void onNext(ByteBuffer buffer) {
				s.onNext(new Buffer(buffer));
			}

			@Override
			public void onError(Throwable t) {
				s.onError(t);
			}

			@Override
			public void onComplete() {
				s.onComplete();
			}
		});
	}

	@Override
	public Protocol getProtocol() {
		return this.serverRequest.getProtocol();
	}

	@Override
	public String getUri() {
		return this.serverRequest.getUri();
	}

	@Override
	public RequestMethod getMethod() {
		return this.serverRequest.getMethod();
	}
}
