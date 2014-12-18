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

package rxweb.netty.client;

import rxweb.Client;
import rxweb.client.ClientHandler;
import rxweb.client.ClientRequest;

/**
 * @author Sebastien Deleuze
 */
public class NettyClient implements Client {

	@Override
	public void get(String uri, ClientHandler handler) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void post(String uri, ClientHandler handler) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void put(String uri, ClientHandler handler) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void delete(String uri, ClientHandler handler) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void request(ClientRequest request, ClientHandler handler) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
