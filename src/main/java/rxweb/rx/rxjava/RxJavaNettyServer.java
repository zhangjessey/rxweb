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

package rxweb.rx.rxjava;

import java.util.concurrent.CompletableFuture;

import rxweb.engine.server.netty.NettyServer;

/**
 * @author Sebastien Deleuze
 */
public class RxJavaNettyServer implements RxJavaServer {

	private NettyServer server;

	public RxJavaNettyServer() {
		this.server = new NettyServer();
	}

	public RxJavaNettyServer(String host, int port) {
		this.server = new NettyServer(host, port);
	}

	@Override
	public CompletableFuture<Void> start() {
		return this.server.start();
	}

	@Override
	public CompletableFuture<Void> stop() {
		return this.server.stop();
	}

	@Override
	public void get(String path, RxJavaServerHandler handler) {
		this.server.get(path, new ServerHandlerAdapter(handler));
	}

	@Override
	public void post(String path, RxJavaServerHandler handler) {
		this.server.post(path, new ServerHandlerAdapter(handler));
	}

	@Override
	public void put(String path, RxJavaServerHandler handler) {
		this.server.put(path, new ServerHandlerAdapter(handler));
	}

	@Override
	public void delete(String path, RxJavaServerHandler handler) {
		this.server.delete(path, new ServerHandlerAdapter(handler));
	}
}
