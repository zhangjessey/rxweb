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

package rxweb.engine.server.netty;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import rxweb.server.ServerResponseHeaders;

/**
 * @author Sebastien Deleuze
 */
public class NettyResponseHeadersAdapter implements ServerResponseHeaders {

	private final HttpResponse nettyResponse;
	private final HttpHeaders nettyHeaders;

	public NettyResponseHeadersAdapter(HttpResponse nettyResponse) {
		this.nettyResponse = nettyResponse;
		this.nettyHeaders = nettyResponse.headers();
	}

	@Override
	public boolean contains(String name) {
		return this.nettyHeaders.contains(name);
	}

	@Override
	public boolean contains(String name, String value, boolean ignoreCaseValue) {
		return this.nettyHeaders.contains(name, value, ignoreCaseValue);
	}

	@Override
	public List<Map.Entry<String, String>> entries() {
		return this.nettyHeaders.entries();
	}

	@Override
	public String get(String name) {
		return this.nettyHeaders.get(name);
	}

	@Override
	public List<String> getAll(String name) {
		return this.nettyHeaders.getAll(name);
	}

	@Override
	public Date getDate() throws ParseException {
		return HttpHeaders.getDate(this.nettyResponse);
	}

	@Override
	public Date getDateHeader(String name) throws ParseException {
		return HttpHeaders.getDateHeader(this.nettyResponse, name);
	}

	@Override
	public String getHost() {
		return HttpHeaders.getHost(this.nettyResponse);
	}

	@Override
	public boolean isEmpty() {
		return this.nettyHeaders.isEmpty();
	}

	@Override
	public Set<String> names() {
		return this.nettyHeaders.names();
	}

	@Override
	public long getContentLength() {
		return 0;
	}

	@Override
	public ServerResponseHeaders add(String name, String value) {
		this.nettyHeaders.add(name, value);
		return this;
	}

	@Override
	public ServerResponseHeaders add(String name, Iterable<String> values) {
		this.nettyHeaders.add(name, values);
		return this;
	}

	@Override
	public ServerResponseHeaders addDateHeader(String name, Date value) {
		HttpHeaders.addDateHeader(this.nettyResponse, name, value);
		return this;
	}

	@Override
	public ServerResponseHeaders clear() {
		this.nettyHeaders.clear();
		return this;
	}

	@Override
	public ServerResponseHeaders remove(String name) {
		this.nettyHeaders.remove(name);
		return this;
	}

	@Override
	public ServerResponseHeaders removeTransferEncodingChunked() {
		HttpHeaders.removeTransferEncodingChunked(this.nettyResponse);
		return this;
	}

	@Override
	public ServerResponseHeaders set(String name, String value) {
		this.nettyHeaders.set(name, value);
		return this;
	}

	@Override
	public ServerResponseHeaders set(String name, Iterable<String> values) {
		this.nettyHeaders.set(name, values);
		return this;
	}

	@Override
	public ServerResponseHeaders contentLength(long length) {
		HttpHeaders.setContentLength(this.nettyResponse, length);
		return this;
	}

	@Override
	public ServerResponseHeaders date(Date value) {
		HttpHeaders.setDate(this.nettyResponse, value);
		return this;
	}

	@Override
	public ServerResponseHeaders dateHeader(String name, Date value) {
		HttpHeaders.setDateHeader(this.nettyResponse, name, value);
		return this;
	}

	@Override
	public ServerResponseHeaders dateHeader(String name, Iterable<Date> values) {
		HttpHeaders.setDateHeader(this.nettyResponse, name, values);
		return this;
	}

	@Override
	public ServerResponseHeaders host(String value) {
		HttpHeaders.setHost(this.nettyResponse, value);
		return this;
	}

	@Override
	public ServerResponseHeaders keepAlive(boolean keepAlive) {
		HttpHeaders.setKeepAlive(this.nettyResponse, keepAlive);
		return this;
	}

	@Override
	public ServerResponseHeaders transferEncodingChunked() {
		HttpHeaders.setTransferEncodingChunked(this.nettyResponse);
		return this;
	}
}
