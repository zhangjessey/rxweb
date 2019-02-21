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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServer;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import rxweb.Server;
import rxweb.mapping.Condition;
import rxweb.mapping.HandlerResolver;
import rxweb.server.DefaultHandlerResolver;

import java.util.concurrent.CompletableFuture;

/**
 * RxNetty powered RxWeb server
 *
 * @author Sebastien Deleuze
 * @author zhangjessey
 */
public class NettyServer implements Server {

    private HandlerResolver handlerResolver = DefaultHandlerResolver.getSingleton();
    HttpServer<ByteBuf, ByteBuf> httpServer;

    public NettyServer() {
        httpServer = HttpServer.newServer(8080).
                channelOption(ChannelOption.SO_KEEPALIVE, true).
                channelOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);


    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public static void main(String[] args) {
        new NettyServer().start();
    }


    @Override
    public CompletableFuture<Void> start() {
        httpServer.start(new Dispatcher()).awaitShutdown();
        return null;
    }

    @Override
    public CompletableFuture<Void> stop() {
        httpServer.shutdown();
        return null;
    }

    @Override
    public void get(String path, RequestHandler handler) {
        addHandler(new Condition<>(HttpMethod.GET, path), handler);
    }

    @Override
    public void post(String path, RequestHandler handler) {
        addHandler(new Condition<>(HttpMethod.POST, path), handler);
    }

    @Override
    public void put(String path, RequestHandler handler) {
        addHandler(new Condition<>(HttpMethod.PUT, path), handler);
    }

    @Override
    public void delete(String path, RequestHandler handler) {
        addHandler(new Condition<>(HttpMethod.DELETE, path), handler);
    }

    @Override
    public void addHandler(Condition<HttpServerRequest> condition, RequestHandler handler) {
        this.handlerResolver.addHandler(condition, handler);
    }
}
