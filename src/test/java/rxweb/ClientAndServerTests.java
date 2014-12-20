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

import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rxweb.client.DefaultClientRequest;
import rxweb.http.Method;
import rxweb.http.Status;
import rxweb.netty.client.NettyClient;
import rxweb.netty.server.NettyServer;

/**
 * @author Sebastien Deleuze
 */
public class ClientAndServerTests {

	private Client client;
	private Server server;

	@Before
	public void setup() {
		server = new NettyServer();
		client = new NettyClient("localhost", 8080);
		server.start();
	}

	@After
	public void tearDown() {
		server.stop();
	}


	@Test
	// TODO Currently it produces the following error : Content stream is already disposed, maybe related to https://github.com/ReactiveX/RxNetty/issues/264
	public void clientAndServer() throws ExecutionException, InterruptedException {

		server.get("/test", (request, response) -> response.status(Status.OK).writeString("Hello World!").flush());

		String result = client.execute(new DefaultClientRequest().uri("/test").method(
				Method.GET)).get().getContentAsString().toBlocking().single();
		Assert.assertEquals("Hello World!", result);
	}

}
