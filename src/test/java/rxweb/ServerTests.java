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

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import reactor.io.buffer.Buffer;
import reactor.rx.Promises;
import rxweb.http.Status;
import rxweb.engine.server.netty.NettyServer;

/**
 * @author Sebastien Deleuze
 */
public class ServerTests {

	private Server server;

	@Before
	public void setup() throws ExecutionException, InterruptedException {
		server = new NettyServer();
		// TODO: replace by server.start().get() when we will use RxJava (it seems buggy with Reactor)
		server.start();
	}

	@After
	public void tearDown() throws ExecutionException, InterruptedException {
		// TODO: replace by server.stop().get() when we will use RxJava (it seems buggy with Reactor)
		server.stop();
	}

	@Test
	public void writeBuffer() throws IOException {
		server.get("/test", (request, response) -> response.status(Status.OK).content(Promises.success(Buffer.wrap("This is a test!").byteBuffer())));
		String content = Request.Get("http://localhost:8080/test").execute().returnContent().asString();
		Assert.assertEquals("This is a test!", content);
	}

	@Test
	public void echo() throws IOException {
		server.post("/test", (request, response) -> response.content(request));
		String content = Request.Post("http://localhost:8080/test").bodyString("This is a test!", ContentType.TEXT_PLAIN).execute().returnContent().asString();
		Assert.assertEquals("This is a test!", content);
	}

}
