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

package rxweb.mapping;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import rxweb.bean.WebRequest;
import rxweb.server.WebRequestHandler;

import java.util.List;

/**
 * 请求解析器
 *
 * @author Sebastien Deleuze
 * @author zhangjessey
 */
public interface HandlerResolver extends HandlerRegistry<HttpServerRequest> {

	/**
	 * 根据request找到对应的RequestHandler
	 * @param request 内部请求对象
	 * @return 符合要求的WebRequestHandler列表
	 */
	List<WebRequestHandler<ByteBuf, ByteBuf>> resolve(WebRequest request);

}