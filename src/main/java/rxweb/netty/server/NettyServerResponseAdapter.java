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

import java.util.concurrent.CompletableFuture;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import rxweb.http.ResponseHeaders;
import rxweb.http.Protocol;
import rxweb.http.Request;
import rxweb.http.Status;
import rxweb.http.Transfer;
import rxweb.server.ServerResponse;
import rxweb.server.ServerResponseHeaders;
import rxweb.util.ObservableUtils;

import org.springframework.util.Assert;

/**
 * @author Sebastien Deleuze
 */
public class NettyServerResponseAdapter implements ServerResponse {

	private final HttpServerResponse<ByteBuf> nettyResponse;
	private final ServerResponseHeaders headers;
	private final Request request;

	public NettyServerResponseAdapter(HttpServerResponse<ByteBuf> nettyResponse,
			Request request) {
		this.nettyResponse = nettyResponse;
		this.headers = new NettyResponseHeadersAdapter(this.nettyResponse.getHeaders());
		this.request = request;
	}

	@Override
	public Request getRequest() {
		return this.request;
	}

	@Override
	public Status getStatus() {
		return Status.valueOf(this.nettyResponse.getStatus().code());
	}

	@Override
	public ServerResponse status(Status status) {
		this.nettyResponse.setStatus(HttpResponseStatus.valueOf(status.getCode()));
		return this;
	}

	@Override
	public ServerResponseHeaders getHeaders() {
		return this.headers;
	}

	@Override
	public Transfer getTransfer() {
		if("chunked".equals(this.headers.get(ResponseHeaders.TRANSFER_ENCODING))) {
			Assert.isTrue(Protocol.HTTP_1_1.equals(this.request.getProtocol()));
			return Transfer.CHUNKED;
		} else if(this.headers.get(ResponseHeaders.TRANSFER_ENCODING) == null) {
			return Transfer.NON_CHUNKED;
		}
		throw new IllegalStateException("Can't determine a valide transfer based on headers and protocol");
	}

	@Override
	public ServerResponse transfer(Transfer transfer) {
		switch (transfer) {
			case EVENT_STREAM:
				throw new IllegalStateException("Transfer " + Transfer.EVENT_STREAM + " is not supported yet");
			case CHUNKED:
				Assert.isTrue(Protocol.HTTP_1_1.equals(this.request.getProtocol()));
				this.header(ResponseHeaders.TRANSFER_ENCODING, "chunked");
				break;
			case NON_CHUNKED:
				this.getHeaders().remove(ResponseHeaders.TRANSFER_ENCODING);
		}
		return this;
	}

	@Override
	public ServerResponse header(String name, String value) {
		this.nettyResponse.getHeaders().set(name, value);
		return this;
	}

	@Override
	public ServerResponse addHeader(String name, String value) {
		this.nettyResponse.getHeaders().add(name, value);
		return this;
	}

	@Override
	public ServerResponse write(ByteBuf content) {
		this.nettyResponse.writeBytes(content);
		return this;
	}

	@Override
	public ServerResponse writeString(String content) {
		this.nettyResponse.writeString(content);
		return this;
	}

	@Override
	public ServerResponse write(Object content) {
		// We need to use converts/transformers here
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public CompletableFuture<Void> flush() {
		return ObservableUtils.fromVoidObservable(this.nettyResponse.flush());
	}

	@Override
	public CompletableFuture<Void> close() {
		return ObservableUtils.fromVoidObservable(this.nettyResponse.close());
	}
}
