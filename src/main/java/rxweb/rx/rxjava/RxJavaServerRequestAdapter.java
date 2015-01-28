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

package rxweb.rx.rxjava;

import java.nio.ByteBuffer;

import org.reactivestreams.Subscriber;
import rx.Observable;
import rx.RxReactiveStreams;
import rxweb.http.Method;
import rxweb.http.Protocol;
import rxweb.server.ServerRequest;
import rxweb.server.ServerRequestHeaders;

/**
 * @author Sebastien Deleuze
 */
public class RxJavaServerRequestAdapter implements RxJavaServerRequest {

	private ServerRequest serverRequest;

	public RxJavaServerRequestAdapter(ServerRequest serverRequest) {
		this.serverRequest = serverRequest;
	}

	@Override
	public ServerRequestHeaders getHeaders() {
		return this.serverRequest.getHeaders();
	}

	@Override
	public Observable<ByteBuffer> getContent() {
		return RxReactiveStreams.toObservable(this.serverRequest);
	}

	@Override
	public void subscribe(Subscriber<? super ByteBuffer> s) {
		this.serverRequest.subscribe(s);
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
	public Method getMethod() {
		return this.serverRequest.getMethod();
	}
}
