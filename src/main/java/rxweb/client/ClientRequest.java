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

import io.netty.buffer.ByteBuf;
import rx.Observable;
import rxweb.http.Method;
import rxweb.http.Protocol;
import rxweb.http.Request;

/**
 * @author Sebastien Deleuze
 */
public interface ClientRequest extends Request {

	ClientRequestHeaders getHeaders();

	ClientRequest uri(String uri);

	ClientRequest protocol(Protocol protocol);

	ClientRequest method(Method method);

	ClientRequest header(String name, String value);

	ClientRequest addHeader(String name, String value);

	ClientRequest accept(String value);

	ClientRequest contentSource(Observable<ByteBuf> value);

	ClientRequest stringContentSource(Observable<String> value);

	Observable<ByteBuf> getContentSource();

}
