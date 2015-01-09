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

import java.util.List;

import io.netty.handler.codec.http.HttpServerCodec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.Environment;
import reactor.io.codec.json.JacksonJsonCodec;
import reactor.io.net.NetServer;
import reactor.io.net.config.ServerSocketOptions;
import reactor.io.net.netty.NettyServerSocketOptions;
import reactor.io.net.netty.tcp.NettyTcpServer;
import reactor.io.net.tcp.spec.TcpServerSpec;
import reactor.rx.Promise;
import reactor.rx.Stream;
import rxweb.server.HandlerChain;
import rxweb.server.Context;
import rxweb.server.ServerHandler;
import rxweb.server.AbstractServer;
import rxweb.server.ServerRequest;
import rxweb.server.ServerResponse;

/**
 * @author Sebastien Deleuze
 */
public class NettyServer extends AbstractServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Environment env = new Environment();

	private final NetServer<ServerRequest, Object> server;

	public NettyServer() {
		ServerSocketOptions options = new NettyServerSocketOptions()
				.pipelineConfigurer(pipeline -> pipeline.addLast(new HttpServerCodec())
						.addLast(new ServerRequestResponseConverter(env)));

		server = new TcpServerSpec<ServerRequest, Object>(NettyTcpServer.class)
				.listen(8080).env(this.env).dispatcher("sync").options(options)
				.consume(ch -> {
					// filter requests by URI via the input Stream
					Stream<ServerRequest> in = ch.in();

					// Implement here the routing
					in.consume(request -> {
						List<ServerHandler> handlers =
								this.handlerResolver.resolve(request);
						if (!handlers.isEmpty()) {
							ServerResponse response =
									new NettyServerResponse(ch, request);
							HandlerChain chain = new HandlerChain(handlers);
							Context context = new Context(request, response, chain);
							chain.next().handle(request, response, context);
						}
					});
				}).get();
	}

	@Override
	public Promise<Boolean> start() {
		return this.server.start();
	}

	@Override
	public Promise<Boolean> stop() {
		return this.server.shutdown();
	}

}
