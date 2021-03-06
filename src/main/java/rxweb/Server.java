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

package rxweb;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import rxweb.mapping.HandlerRegistry;
import rxweb.server.WebRequestHandler;

/**
 * 服务器接口
 *
 * @author Sebastien Deleuze
 * @author zhangjessey
 */
public interface Server extends HandlerRegistry<HttpServerRequest> {

	/**
	 * 启动
	 */
	void start();

	/**
	 * 关闭
	 */
	void stop();

	/**
	 * 配置get方法
	 * @param path 路径
	 * @param handler 具体的WebRequestHandler
	 * @return Server对象，可以链式调用
	 */
	Server get(final String path, final WebRequestHandler<ByteBuf, ByteBuf> handler);

	/**
	 * 配置post方法
	 * @param path 路径
	 * @param handler 具体的WebRequestHandler
	 * @return Server对象，可以链式调用
	 */
	Server post(final String path, final WebRequestHandler<ByteBuf, ByteBuf> handler);

	/**
	 * 配置put方法
	 * @param path 路径
	 * @param handler 具体的WebRequestHandler
	 * @return Server对象，可以链式调用
	 */
	Server put(final String path, final WebRequestHandler<ByteBuf, ByteBuf> handler);

	/**
	 * 配置delete方法
	 * @param path 路径
	 * @param handler 具体的WebRequestHandler
	 * @return Server对象，可以链式调用
	 */
	Server delete(final String path, final WebRequestHandler<ByteBuf, ByteBuf> handler);

}
