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
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.reactivestreams.Publisher;
import reactor.io.buffer.Buffer;
import reactor.io.net.NetChannel;
import reactor.rx.Promise;
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
		this.headers.set(name, value);
		return this;
	}

	@Override
	public ServerResponse addHeader(String name, String value) {
		this.headers.add(name, value);
		return this;
	}

	/**
	 * For the moment, {@link Buffer} and {@link String} are supported
	 */
	@Override
	public Promise<Void> write(Object content) {
		// TODO: We need to use converts/transformers here
		ByteBuf nettyBuffer;
		if(content instanceof Buffer) {
			// TODO: Need to be able to retreive Netty Channel from Reactor NettyNetChannel in order to use pooled buffers
			nettyBuffer = Unpooled.wrappedBuffer(((Buffer)content).byteBuffer());
		} else if(content instanceof String) {
			nettyBuffer = Unpooled.wrappedBuffer(
					((String) content).getBytes(StandardCharsets.UTF_8));
		} else {
			throw new UnsupportedOperationException("Not implemented yet");
		}

		HttpContent httpContent = new DefaultHttpContent(nettyBuffer);
		if(!this.statusAndHeadersSent) {
			this.statusAndHeadersSent = true;
			Promise<Void> headersPromise = this.channel.send(this);
			Promise<Void> contentPromise = this.channel.send(httpContent);
			// TODO: Using a workaround while waiting resolution of https://github.com/reactor/reactor/issues/416
			return Streams.zip(headersPromise, contentPromise, tuple -> tuple.t1).next();
		}
		return this.channel.send(httpContent);

	}

	@Override
	public ServerResponse source(Publisher<Object> publisher) {
		Streams.create(publisher).observe(content -> this.write(content));
		return this;
	}

	@Override
	public Promise<Void> flush() {
		return this.channel.send(this);
	}

	@Override
	public Promise<Boolean> close() {
		if(!this.statusAndHeadersSent) {
			this.statusAndHeadersSent = true;
			Promise<Void> headersPromise = this.channel.send(this);
			Promise<Boolean> contentPromise = this.channel.close();
			return Streams.zip(headersPromise, contentPromise, tuple -> tuple.t2).next();
		}
		return this.channel.close();
	}

	public HttpResponse getNettyResponse() {
		return nettyResponse;
	}
}
