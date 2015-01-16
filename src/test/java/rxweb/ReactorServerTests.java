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

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import reactor.io.buffer.Buffer;
import rxweb.http.Status;
import rxweb.rx.reactor.ReactorNettyServer;
import rxweb.rx.reactor.ReactorServer;

/**
 * @author Sebastien Deleuze
 */
public class ReactorServerTests {

	private ReactorServer server;

	@Before
	public void setup() {
		server = new ReactorNettyServer();
		server.start();
	}

	@After
	public void tearDown() {
		server.stop();
	}

	@Test
	public void writePojo() throws IOException {
		server.get("/test", (request, response) -> response.status(Status.OK).write(new User("Brian", "Clozel")));
		String content = Request.Get("http://localhost:8080/test").execute().returnContent().asString();
		Assert.assertEquals("{\"firstname\":\"Brian\",\"lastname\":\"Clozel\"}", content);
	}

	@Test
	public void writeByteBuffer() throws IOException {
		server.get("/test", (request, response) -> response.status(Status.OK).write(Buffer.wrap("This is a test!").byteBuffer()));
		String content = Request.Get("http://localhost:8080/test").execute().returnContent().asString();
		Assert.assertEquals("This is a test!", content);
	}

	@Test
	public void writeBuffer() throws IOException {
		server.get("/test", (request, response) -> response.status(Status.OK).write(Buffer.wrap("This is a test!")));
		String content = Request.Get("http://localhost:8080/test").execute().returnContent().asString();
		Assert.assertEquals("This is a test!", content);
	}

	@Test // TODO: FIXME
	public void echoStream() throws IOException {
		server.post("/test", (request, response) -> request);
		String content = Request.Post("http://localhost:8080/test").bodyString("This is a test!", ContentType.TEXT_PLAIN).execute().returnContent().asString();
		Assert.assertEquals("This is a test!", content);
	}

	@Test // TODO: FIXME
	public void echo() throws IOException {
		server.post("/test", (request, response) -> request.getContent());
		String content = Request.Post("http://localhost:8080/test").bodyString("This is a test!", ContentType.TEXT_PLAIN).execute().returnContent().asString();
		Assert.assertEquals("This is a test!", content);
	}

	public static class User {

		public User() {
		}

		public User(String firstname, String lastname) {
			this.firstname = firstname;
			this.lastname = lastname;
		}

		private String firstname;
		private String lastname;

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}
	}

}
