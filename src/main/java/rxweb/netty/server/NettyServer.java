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
import rxweb.server.ServerHandler;
import rxweb.http.Request;
import rxweb.http.Response;
import rxweb.server.AbstractServer;
import rxweb.server.ServerRequest;
import rxweb.server.ServerResponse;

/**
 * @author Sebastien Deleuze
 */
public class NettyServer extends AbstractServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private HttpServer<ByteBuf, ByteBuf> httpServer;

	@Override
	public void start() {
		// TODO: We need to customize the PipelineConfigurator for the behavior we want.
		// We want the handle to be called just after the request headers have been received
		// It may depend on what kind of data we want to deal with (bytes, string or Object)
		httpServer = RxNetty.createHttpServer(port, (nettyRequest, nettyResponse) -> {
			// TODO: need to create our own ByteBuf to avoid exposing Netty types in our API
			ServerRequest request = new NettyServerRequestAdapter(nettyRequest);
			ServerResponse response = new NettyServerResponseAdapter(nettyResponse, request);
			List<ServerHandler> handlers = this.handlerResolver.resolve(request);
			if(!handlers.isEmpty()) {
				Observable<Void> handle = handlers.get(0).handle(request, response);
				for (int i = 1; i < handlers.size(); i++) {
					handle.mergeWith(handlers.get(i).handle(request, response));
				}
				// Not sure, need to check
				handle.doOnCompleted(() -> response.close());
				return handle;
			}
			return null;
		});
		httpServer.start();
	}


	@Override
	public void stop() {
		try {
			httpServer.shutdown();
		}
		catch (InterruptedException e) {
			logger.error("Error while shutting down the RxWeb server", e);
		}
	}

}
