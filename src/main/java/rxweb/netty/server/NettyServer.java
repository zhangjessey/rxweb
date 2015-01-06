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

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.server.HttpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rxweb.server.HandlerChain;
import rxweb.server.Context;
import rxweb.server.ServerHandler;
import rxweb.server.AbstractServer;
import rxweb.server.ServerRequest;
import rxweb.server.ServerResponse;

/**
 * TODO: Replace RxNetty by Reactor + Netty, maybe https://github.com/pk11/reactor-http-demo/blob/master/src/main/java/com/github/pk11/rnio/HttpServer.java could help
 * @author Sebastien Deleuze
 */
public class NettyServer extends AbstractServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private HttpServer<ByteBuf, ByteBuf> nettyServer;

	@Override
	public void start() {
		nettyServer = RxNetty.createHttpServer(port, (nettyRequest, nettyResponse) -> {
			ServerRequest request = new NettyServerRequestAdapter(nettyRequest);
			ServerResponse response = new NettyServerResponseAdapter(nettyResponse, request);
			List<ServerHandler> handlers = this.handlerResolver.resolve(request);
			if(handlers.isEmpty()) {
				return Observable.empty();
			}
			HandlerChain chain = new HandlerChain(handlers);
			Context context = new Context(request, response, chain);
			chain.next().handle(request, response, context);

			// TODO: With the next() approach we have, I think we can't easily return the right Observable
			return Observable.empty();
		});
		nettyServer.start();
	}


	@Override
	public void stop() {
		try {
			nettyServer.shutdown();
		}
		catch (InterruptedException e) {
			logger.error("Error while shutting down the RxWeb server", e);
		}
	}

}
