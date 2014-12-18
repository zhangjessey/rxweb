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

import org.junit.Before;
import org.junit.Test;
import rxweb.netty.client.NettyClient;

/**
 * @author Sebastien Deleuze
 */
public class ClientTests {

	private Client client;

	@Before
	public void setup() {
		this.client = new NettyClient();
	}

	@Test
	public void simpleGet() {
		client.get("/test", response -> response.getContentAsString().subscribe(System.out::println));
	}

}
