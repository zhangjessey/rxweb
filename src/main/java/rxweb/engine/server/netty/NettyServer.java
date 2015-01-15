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
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.Environment;
import reactor.io.net.NetServer;
import reactor.io.net.config.ServerSocketOptions;
import reactor.io.net.netty.NettyServerSocketOptions;
import reactor.io.net.netty.tcp.NettyTcpServer;
import reactor.io.net.tcp.spec.TcpServerSpec;
import reactor.rx.Promise;
import reactor.rx.Stream;
import reactor.rx.Streams;
import rxweb.converter.Converter;
import rxweb.server.AbstractServer;
import rxweb.server.ServerRequest;
import rxweb.server.ServerResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Netty powered Spring RxWeb server
 *
 * @author Sebastien Deleuze
 */
public class NettyServer extends AbstractServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Environment env = new Environment();

	private final NetServer<ServerRequest, Object> server;

	public NettyServer() {
		ServerSocketOptions options = new NettyServerSocketOptions()
				.pipelineConfigurer(pipeline -> pipeline.addLast(new LoggingHandler()).addLast(new HttpServerCodec()).addLast(new NettyServerCodecHandlerAdapter(env)));

		server = new TcpServerSpec<ServerRequest, Object>(NettyTcpServer.class).listen(8080).env(this.env).dispatcher
				("sync").options(options).consume(ch -> {
			// filter requests by URI via the input Stream
			Stream<ServerRequest> in = ch.in();

			// Implement here the routing
			in.consume(request -> {
				ServerResponse response = new NettyServerResponseAdapter(ch, request);
				List<Publisher<?>> publishers = this.handlerResolver.resolve(request).stream().map(handler -> {
					request.setConverterResolver(this.converterResolver);
					return handler.handle(request, response);
				}).collect(Collectors.toList());

				// Streams.concat(publishers) execute handlers in order (handler chain)
				// Streams.merge(publishers) all handlers contribute to the output stream (if on the same thread
				// so maybe we need to specify another dispatcher)
				Streams.concat(publishers).map(data -> {
					// TODO: handle media type
					Converter converter = this.converterResolver.resolveWriter(data.getClass(), null);
					return converter.write(data, null);
				}).flatMap(buffer -> {
					if (response.isStatusAndHeadersSent()) {
						return ch.send(buffer);
					}
					else {
						response.setStatusAndHeadersSent(true);
						return Streams.concat(ch.send(response), ch.send(buffer));
					}
				}).consume(
				/* ignore onNext */ null,
				/* ignore errors */ Throwable::printStackTrace,
						/* close on Complete */ v -> {
					if (response.isStatusAndHeadersSent()) {
						ch.close();
					} else {
						response.setStatusAndHeadersSent(true);
						ch.send(response).onComplete(w -> ch.close());
					}
				});
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
