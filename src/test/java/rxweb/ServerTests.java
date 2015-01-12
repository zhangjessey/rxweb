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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import rxweb.http.Status;
import rxweb.netty.server.NettyServer;

/**
 * @author Sebastien Deleuze
 */
public class ServerTests {

	private Server server;

	@Before
	public void setup() {
		server = new NettyServer();
		server.start();
	}

	@After
	public void tearDown() {
		server.stop();
	}

	@Test
	public void server() throws IOException {

		OkHttpClient client = new OkHttpClient();

		// TODO: how to auto-close the request ? We could add an auto-close handler at the end of the chain but it would require calling context.next() ...
		server.get("/test", (request, response, context) -> response.status(Status.OK).write("Hello World!").onSuccess(v -> response.close()));

		Request request = new Request.Builder().url("http://localhost:8080/test").build();

		Response response = client.newCall(request).execute();
		Assert.assertEquals("Hello World!", response.body().string());
	}

}
