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

package rxweb.server;

import java.util.concurrent.CompletableFuture;

import io.netty.buffer.ByteBuf;
import rx.Observable;
import rxweb.http.Request;
import rxweb.http.Response;
import rxweb.http.Status;
import rxweb.http.Transfer;

/**
 * @author Sebastien Deleuze
 */
public interface ServerResponse extends Response {

	Request getRequest();

	ServerResponse status(Status status);

	ServerResponseHeaders getHeaders();

	ServerResponse header(String name, String value);

	ServerResponse addHeader(String name, String value);

	ServerResponse transfer(Transfer transfer);

	CompletableFuture<Void> writeRaw(ByteBuf content);

	ServerResponse rawSource(Observable<ByteBuf> value);

	Observable<ByteBuf> getRawSource();

	CompletableFuture<Void> writeString(String content);

	ServerResponse stringSource(Observable<String> value);

	CompletableFuture<Void> write(Object content);

	ServerResponse source(Observable<Object> value);

	CompletableFuture<Void> close();

}
