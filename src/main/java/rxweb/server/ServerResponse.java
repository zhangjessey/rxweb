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

import org.reactivestreams.Publisher;
import rxweb.http.Response;
import rxweb.http.Transfer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * @author Sebastien Deleuze
 */
public interface ServerResponse extends Response {

	ServerRequest getRequest();

	ServerResponse status(HttpStatus status);

	HttpHeaders getHeaders();

	ServerResponse header(String name, String value);

	ServerResponse addHeader(String name, String value);

	ServerResponse transfer(Transfer transfer);

	Publisher<?> write(Object content);

	boolean isStatusAndHeadersSent();

	void setStatusAndHeadersSent(boolean statusAndHeadersSent);

}
