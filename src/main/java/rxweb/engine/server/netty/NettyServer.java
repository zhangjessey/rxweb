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

import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import reactor.core.dispatch.SynchronousDispatcher;
import reactor.io.net.NetChannel;
import reactor.io.net.config.ServerSocketOptions;
import reactor.io.net.netty.NettyServerSocketOptions;
import reactor.io.net.netty.tcp.NettyTcpServer;
import reactor.io.net.tcp.spec.TcpServerSpec;
import rxweb.server.AbstractServer;
import rxweb.server.ServerRequest;
import rxweb.server.ServerResponse;

/**
 * Netty powered Spring RxWeb server
 *
 * @author Sebastien Deleuze
 */
public class NettyServer extends AbstractServer {

	public NettyServer() {
		init();
	}

	public NettyServer(String host, int port) {
		super(host, port);
		init();
	}

	private void init() {
		ServerSocketOptions options = new NettyServerSocketOptions().pipelineConfigurer(pipeline -> pipeline.addLast(new LoggingHandler()).addLast(new HttpServerCodec()).addLast(new NettyServerCodecHandlerAdapter(env)));

		server = new TcpServerSpec<ServerRequest, Object>(NettyTcpServer.class).listen(this.host, this.port)
				.env(this.env).dispatcher(SynchronousDispatcher.INSTANCE).options(options)
				.consume(ch -> ch.in().consume(request -> this.handleRequest(ch, request))).get();
	}

	@Override
	protected ServerResponse createResponse(NetChannel<ServerRequest, Object> ch, ServerRequest request) {
		return new NettyServerResponseAdapter(ch, request);
	}
}
