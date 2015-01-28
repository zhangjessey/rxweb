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

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.*;
import reactor.Environment;
import reactor.core.dispatch.SynchronousDispatcher;
import reactor.io.buffer.Buffer;
import reactor.rx.broadcast.Broadcaster;
import rxweb.server.ServerRequest;
import rxweb.support.Assert;

/**
 * Conversion between Netty types ({@link HttpRequest}, {@link HttpResponse}, {@link HttpContent} and {@link LastHttpContent})
 * and Spring RxWeb types ({@link NettyServerResponseAdapter}, {@link NettyServerRequestAdapter} and {@link Buffer}).
 *
 * @author Sebastien Deleuze
 */
public class NettyServerCodecHandlerAdapter extends ChannelDuplexHandler {

	private final Environment env;
	private ServerRequest request;
	private Broadcaster<ByteBuffer> requestContentStream;

	public NettyServerCodecHandlerAdapter(Environment env) {
		this.env = env;
		this.requestContentStream = Broadcaster.create(this.env, SynchronousDispatcher.INSTANCE);
	}

	/**
	 * Create a {@link NettyServerRequestAdapter} when a {@link HttpRequest} is received, and use
	 * @ {@link Broadcaster} to send the content as a request stream.
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Class<?> messageClass = msg.getClass();
		if (HttpRequest.class.isAssignableFrom(messageClass)) {
			this.request = new NettyServerRequestAdapter((HttpRequest) msg, this.requestContentStream);
			super.channelRead(ctx, request);
		} else if (HttpContent.class.isAssignableFrom(messageClass)) {
			Assert.notNull(this.request);
			ByteBuf content = ((ByteBufHolder) msg).content();
			this.requestContentStream.onNext(content.nioBuffer());
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
		} else if (ByteBuffer.class.isAssignableFrom(messageClass)) {
			ByteBuf byteBuf = Unpooled.copiedBuffer((ByteBuffer) msg);
			super.write(ctx, new DefaultHttpContent(byteBuf), promise);
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
