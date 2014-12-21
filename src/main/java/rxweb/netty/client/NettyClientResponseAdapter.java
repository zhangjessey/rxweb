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

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import rx.Observable;
import rxweb.client.ClientResponse;
import rxweb.client.ClientResponseHeaders;
import rxweb.http.Status;
import rxweb.http.Transfer;

/**
 * @author Sebastien Deleuze
 */
public class NettyClientResponseAdapter implements ClientResponse {

	private final HttpClientResponse<ByteBuf> nettyResponse;

	public NettyClientResponseAdapter(HttpClientResponse<ByteBuf> response) {
		this.nettyResponse = response;
	}

	@Override
	public ClientResponseHeaders getHeaders() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Observable<ByteBuf> getRawContent() {
		return this.nettyResponse.getContent();
	}

	@Override
	public <T> Observable<T> getContent(Class<T> clazz) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Observable<String> getStringContent() {
		return this.nettyResponse.getContent().map((content) -> content.toString(StandardCharsets.UTF_8));
	}

	@Override
	public Status getStatus() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Transfer getTransfer() {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
