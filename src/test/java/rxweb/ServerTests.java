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

		// TODO: how to auto-close the request ? We could add an auto-close handler at the end of the chain but it would require calling context.next() ...
		server.get("/test", (request, response) -> response.status(Status.OK).write(new User("Brian", "Clozel")));

		String content = Request.Get("http://localhost:8080/test"). execute().returnContent().asString();

		Assert.assertEquals("{\"firstname\":\"Brian\",\"lastname\":\"Clozel\"}", content);
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
