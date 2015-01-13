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

import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.reactivestreams.Publisher;
import reactor.io.net.NetChannel;
import reactor.rx.Promise;
import reactor.rx.Promises;
import reactor.rx.Streams;
import rxweb.http.ResponseHeaders;
import rxweb.http.Protocol;
import rxweb.http.Status;
import rxweb.http.Transfer;
import rxweb.server.ServerRequest;
import rxweb.server.ServerResponse;
import rxweb.server.ServerResponseHeaders;

import org.springframework.util.Assert;

/**
 * @author Sebastien Deleuze
 */
public class NettyServerResponse implements ServerResponse {

	private final NetChannel<ServerRequest, Object>  channel;
	private final HttpResponse nettyResponse;
	private final ServerRequest request;
	private final ServerResponseHeaders headers;
	private boolean statusAndHeadersSent = false;

	public NettyServerResponse(NetChannel<ServerRequest, Object> channel, ServerRequest request) {
		this.nettyResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		this.headers = new NettyResponseHeadersAdapter(this.nettyResponse);
		this.channel = channel;
		this.request = request;
	}

	@Override
	public ServerRequest getRequest() {
		return this.request;
	}

	@Override
	public Status getStatus() {
		return Status.valueOf(this.nettyResponse.getStatus().code());
	}

	@Override
	public ServerResponse status(Status status) {
		if(this.statusAndHeadersSent) {
			throw new IllegalStateException("Status and headers already sent");
		}
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
		if(this.statusAndHeadersSent) {
			throw new IllegalStateException("Status and headers already sent");
		}
		this.headers.set(name, value);
		return this;
	}

	@Override
	public ServerResponse addHeader(String name, String value) {
		if(this.statusAndHeadersSent) {
			throw new IllegalStateException("Status and headers already sent");
		}
		this.headers.add(name, value);
		return this;
	}

	@Override
	public Promise<?> write(Object content) {
		return Promises.success(content);
	}

	public HttpResponse getNettyResponse() {
		return nettyResponse;
	}

	public boolean isStatusAndHeadersSent() {
		return statusAndHeadersSent;
	}

	public void setStatusAndHeadersSent(boolean statusAndHeadersSent) {
		this.statusAndHeadersSent = statusAndHeadersSent;
	}
}
