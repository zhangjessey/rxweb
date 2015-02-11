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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Subscriber;
import rxweb.Server;
import rxweb.http.Method;
import rxweb.http.Request;
import rxweb.mapping.Condition;
import rxweb.mapping.HandlerResolver;
import rxweb.mapping.MappingCondition;
import rxweb.server.DefaultHandlerResolver;
import rxweb.server.ServerHandler;
import rxweb.server.ServerRequest;
import rxweb.server.ServerResponse;
import rxweb.support.CompletableFutureUtils;

/**
 * Netty powered Spring RxWeb server
 *
 * @author Sebastien Deleuze
 */
public class NettyServer implements Server {

	protected final ServerBootstrap bootstrap;
	protected enum ServerState {Created, Starting, Started, Shutdown};
	private final HandlerResolver handlerResolver = new DefaultHandlerResolver();
	protected final AtomicReference<ServerState> serverStateRef;
	protected int port = 8080;
	private ChannelFuture bindFuture;


	public NettyServer() {
		this.serverStateRef = new AtomicReference<ServerState>(ServerState.Created);
		this.bootstrap = new ServerBootstrap();
		init();
	}

	public NettyServer(String host, int port) {
		this();
		this.port = port;
		init();
	}

	private void init() {
			this.bootstrap
					.option(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
					.group(new NioEventLoopGroup())
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(new LoggingHandler()).addLast(new HttpServerCodec()).addLast(new NettyServerCodecHandlerAdapter()).addLast(new ChannelInboundHandlerAdapter() {

								private CompletableFuture<Channel> headersSent;

								private final Logger logger = LoggerFactory.getLogger(getClass());

								@Override
								public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
									if (msg instanceof ServerRequest) {
										handleRequest(ctx, (ServerRequest) msg);
									}
									else {
										super.channelRead(ctx, msg);
									}
								}

								private void handleRequest(ChannelHandlerContext ctx, ServerRequest request) {
									List<ServerHandler> handlers = handlerResolver.resolve(request);
									if (handlers.isEmpty()) {
										this.logger.info("No handler found for request " +
												request.getUri());
									}
									// In order to keep simple, we take the first handler
									ServerHandler handler = handlers.get(0);
									ServerResponse response = new NettyServerResponseAdapter(request);
									handler.handle(request, response);

									response.getContent().subscribe(new Subscriber<ByteBuffer>() {

										@Override
										public void onNext(ByteBuffer buffer) {
											if (headersSent != null) {
												ctx.writeAndFlush(buffer);
											}
											else {
												headersSent = CompletableFutureUtils.fromChannelFuture(ctx.write(response));

												headersSent.handle((channel, t) -> {
													if (channel != null) {
														response.setStatusAndHeadersSent(true);
														ctx.write(buffer);
													}
													else {
														logger.error(t.toString());
													}
													return channel;
												});
											}
										}

										@Override
										public void onError(Throwable e) {
											logger.error(
													"Error in response content observable: " +
															e);
										}

										@Override
										public void onCompleted() {
											if (response.isStatusAndHeadersSent()) {
												ctx.write(new DefaultLastHttpContent());
												ctx.flush();
												ctx.close();
											}
											else if (headersSent != null) {
												headersSent.thenRun(() -> {
													ctx.write(new DefaultLastHttpContent());
													ctx.flush();ctx.close();
												});
											}
											else {
												CompletableFutureUtils.fromChannelFuture(ctx.write(response)).thenRun(() -> {
													response.setStatusAndHeadersSent(true);
													ctx.write(new DefaultLastHttpContent());
													ctx.flush();
													ctx.close();
												});
											}
										}

									});
								}


							});
						}


					});
	}



	@Override
	public void addHandler(final Condition<Request> condition, final ServerHandler handler) {
		this.handlerResolver.addHandler(condition, handler);
	}

	@Override
	public void get(final String path, final ServerHandler handler) {
		addHandler(MappingCondition.Builder.from(path).method(Method.GET).build(), handler);
	}

	@Override
	public void post(final String path, final ServerHandler handler) {
		addHandler(MappingCondition.Builder.from(path).method(Method.POST).build(), handler);
	}

	@Override
	public void put(final String path, final ServerHandler handler) {
		addHandler(MappingCondition.Builder.from(path).method(Method.PUT).build(), handler);
	}

	@Override
	public void delete(final String path, final ServerHandler handler) {
		addHandler(MappingCondition.Builder.from(path).method(Method.DELETE).build(), handler);
	}

	@Override
	public CompletableFuture<Void> start() {
		if (!serverStateRef.compareAndSet(ServerState.Created, ServerState.Starting)) {
			throw new IllegalStateException("Server already started");
		}

		this.bindFuture = this.bootstrap.bind(this.port);
		this.bindFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception {
				if(channelFuture.isSuccess()) {
					serverStateRef.set(ServerState.Started);
				}
			}
		});
		return CompletableFutureUtils.fromChannelFuture(this.bindFuture).thenRun(() -> {
		});
	}

	@Override
	public CompletableFuture<Void> stop() {
		if (!serverStateRef.compareAndSet(ServerState.Started, ServerState.Shutdown)) {
			throw new IllegalStateException("The server is already shutdown.");
		}
		ChannelFuture closeFuture = this.bindFuture.channel().close();
		return CompletableFutureUtils.fromChannelFuture(closeFuture).thenRun(() -> {
		});

	}
}
