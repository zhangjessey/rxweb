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

import java.util.concurrent.CompletableFuture;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import rxweb.Client;
import rxweb.client.ClientRequestHolder;
import rxweb.client.ClientRequest;
import rxweb.client.ClientResponse;
import rxweb.util.ObservableUtils;

/**
 * @author Sebastien Deleuze
 */
public class NettyClient implements Client {

	HttpClient<ByteBuf, ByteBuf> nettyClient;

	public NettyClient(String host, int port) {
		this.nettyClient = RxNetty.<ByteBuf, ByteBuf>newHttpClientBuilder("localhost", 8080).build();
	}

	@Override
	public CompletableFuture<ClientResponse> execute(ClientRequest request) {
		HttpClientRequest<ByteBuf> nettyRequest = HttpClientRequest.create(HttpMethod.valueOf(request.getMethod().getName()), request.getUri());
		nettyRequest.withContentSource(request.getRawSource());
		CompletableFuture<HttpClientResponse<ByteBuf>> nettyResponseFuture = ObservableUtils.fromSingleObservable(
				this.nettyClient.submit(nettyRequest));
		return nettyResponseFuture.thenApply(nettyResponse -> new NettyClientResponseAdapter(nettyResponse));
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
