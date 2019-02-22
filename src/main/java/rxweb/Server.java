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

import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import rxweb.mapping.HandlerRegistry;

/**
 * @author Sebastien Deleuze
 * @author zhangjessey
 */
public interface Server extends HandlerRegistry<HttpServerRequest> {

	/** Complete when the server is started **/
	void start();

	/** Complete when the server is stopped **/
	void stop();

	void get(final String path, final RequestHandler handler);

	void post(final String path, final RequestHandler handler);

	void put(final String path, final RequestHandler handler);

	void delete(final String path, final RequestHandler handler);

}
