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

package rxweb.client;

import rxweb.http.Response;

import java.nio.ByteBuffer;

/**
 * @author Sebastien Deleuze
 * @author zhangjessey
 */
public interface ClientResponse extends Response {

	ClientResponseHeaders getHeaders();

	/** Return a stream of raw content chunks **/
	ByteBuffer getContentStream();

	/** Return a scalar raw content **/
	ByteBuffer getContent();


	/** Return a stream of POJO contents using converters (1 chunk = 1 POJO) **/
	<T> T getContentStream(Class<T> clazz);


	/** Return a scalar POJO content using converters **/
	<T> T getContent(Class<T> clazz);
}
