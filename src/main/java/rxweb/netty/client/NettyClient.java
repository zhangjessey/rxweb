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
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import reactor.rx.Promise;
import rx.Observable;
import rxweb.Client;
import rxweb.client.ClientRequestHolder;
import rxweb.client.ClientRequest;
import rxweb.client.ClientResponse;
import rxweb.util.ObservableUtils;

/**
 * TODO: Replace RxNetty by Reactor + Netty,
 * @author Sebastien Deleuze
 */
public class NettyClient implements Client {

	HttpClient<ByteBuf, ByteBuf> nettyClient;

	public NettyClient(String host, int port) {
		this.nettyClient = RxNetty.<ByteBuf, ByteBuf>newHttpClientBuilder("localhost", 8080).build();
	}

	@Override
	public Promise<ClientResponse> execute(ClientRequest request) {
		HttpClientRequest<ByteBuf> nettyRequest = HttpClientRequest.create(HttpMethod.valueOf(request.getMethod().getName()), request.getUri());
		if(request.getRawSource() != null) {
			CompositeByteBuf compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
			request.getRawSource().observe(byteArray -> compositeByteBuf.writeBytes(byteArray.asBytes()));
			nettyRequest.withContentSource(Observable.just(compositeByteBuf));
		};
		Promise<ClientResponse> nettyResponseFuture =
				ObservableUtils.fromSingleObservable(this.nettyClient.submit(nettyRequest).map(
						nettyResponse -> new NettyClientResponseAdapter(nettyResponse)));
		return nettyResponseFuture;
	}

	@Override
	public ClientRequestHolder get(String uri) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHolder post(String uri) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHolder put(String uri) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHolder delete(String uri) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
