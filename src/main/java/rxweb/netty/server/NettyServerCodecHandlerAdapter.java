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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import reactor.Environment;
import reactor.io.buffer.Buffer;
import reactor.rx.Streams;
import reactor.rx.stream.Broadcaster;
import rxweb.server.ServerRequest;

import org.springframework.util.Assert;

/**
 * Conversion between Netty types ({@link HttpRequest}, {@link HttpResponse}, {@link HttpContent} and {@link LastHttpContent})
 * and Spring RxWeb types ({@link NettyServerResponseAdapter}, {@link NettyServerRequestAdapter} and {@link Buffer}).
 *
 * @author Sebastien Deleuze
 */
public class NettyServerCodecHandlerAdapter extends ChannelDuplexHandler {

	private final Environment env;
	private ServerRequest request;
	private Broadcaster<Buffer> requestContentStream;

	public NettyServerCodecHandlerAdapter(Environment env) {
		this.env = env;
	}

	/**
	 * Create a {@link NettyServerRequestAdapter} when a {@link HttpRequest} is received, and use
	 * @ {@link Broadcaster} to send the content as a request stream.
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Class<?> messageClass = msg.getClass();

		if (HttpRequest.class.isAssignableFrom(messageClass)) {
			this.requestContentStream = Streams.broadcast(this.env);
			this.request = new NettyServerRequestAdapter((HttpRequest) msg, this.requestContentStream);
			super.channelRead(ctx, request);
		} else if (HttpContent.class.isAssignableFrom(messageClass)) {
			Assert.notNull(this.request);
			ByteBuf content = ((ByteBufHolder) msg).content();
			this.requestContentStream.onNext(new Buffer(content.nioBuffer()));
			if (LastHttpContent.class.isAssignableFrom(messageClass)) {
				this.requestContentStream.onComplete();
			}
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		Class<?> messageClass = msg.getClass();

		if (NettyServerResponseAdapter.class.isAssignableFrom(messageClass)) {
			NettyServerResponseAdapter response = (NettyServerResponseAdapter) msg;
			super.write(ctx, response.getNettyResponse(), promise);
		// Reactor Netty integration has already done the conversion from Reactor Buffer to netty ByteBuf
		} else if (ByteBuf.class.isAssignableFrom(messageClass)) {
			super.write(ctx, new DefaultHttpContent((ByteBuf)msg), promise);
		} else {
			super.write(ctx, msg, promise); // pass through, since we do not understand this message.
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
		ctx.pipeline().flush(); // If there is nothing to flush, this is a short-circuit in netty.
	}

}
