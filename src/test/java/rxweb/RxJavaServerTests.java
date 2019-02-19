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


import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rxweb.engine.server.netty.NettyServer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * @author Sebastien Deleuze
 * @author zhangjessey
 */
public class RxJavaServerTests {

	private NettyServer nettyServer;

	@Before
	public void setup() {
		nettyServer = new NettyServer();
		new Thread(() -> {nettyServer.start();}).start();

	}

	@After
	public void tearDown() {
		nettyServer.stop();
	}

	@Test
	public void writeBuffer() throws IOException {

		String content = Request.Get("http://localhost:8080/test").execute().returnContent().asString();
		Assert.assertEquals("no match!", content);
	}


	@Test
	public void controllerNoParam() throws IOException {
		String content = Request.Get("http://localhost:8080/hi").execute().returnContent().asString();
		Assert.assertEquals("hello", content);
	}

	@Test
	public void controllerWithParam() throws IOException {
		String content = Request.Get("http://localhost:8080/hehe?p1=1&p2=2").execute().returnContent().asString();
		Assert.assertEquals("hahap1:p2", content);
	}

	@Test
	public void postTest() throws IOException {
		//form-data暂不支持
		List<BasicNameValuePair> list = Collections.singletonList(new BasicNameValuePair("b", "2"));
		String content = Request.Post("http://localhost:8080/postTest/10?a=1").bodyForm(list, Charset.forName("UTF-8")).execute().returnContent().asString();
		Assert.assertEquals("echo110", content);
	}

	@Test
	public void deleteTest() throws IOException {
		List<BasicNameValuePair> list = Collections.singletonList(new BasicNameValuePair("b", "2"));
		String content = Request.Delete("http://localhost:8080/postTest/10?a=1").execute().returnContent().asString();
		Assert.assertEquals("echo110", content);
	}

	@Test
	public void putTest() throws IOException {
		List<BasicNameValuePair> list = Collections.singletonList(new BasicNameValuePair("b", "2"));
		String content = Request.Put("http://localhost:8080/postTest/10?a=1").execute().returnContent().asString();
		Assert.assertEquals("echo110", content);
	}

}
