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

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.netty.protocol.http.client.HttpRequestHeaders;
import rxweb.client.ClientRequestHeaders;

/**
 * @author Sebastien Deleuze
 */
public class NettyRequestHeadersAdapter implements ClientRequestHeaders {

	private HttpRequestHeaders nettyHeaders;

	public NettyRequestHeadersAdapter(HttpRequestHeaders nettyHeaders) {
		this.nettyHeaders = nettyHeaders;
	}

	@Override
	public void add(String name, String value) {
		this.nettyHeaders.add(name, value);
	}

	@Override
	public void add(String name, Iterable<String> values) {
		this.nettyHeaders.add(name, values);
	}

	@Override
	public void addDateHeader(String name, Date value) {
		this.nettyHeaders.addDateHeader(name, value);
	}

	@Override
	public void clear() {
		this.nettyHeaders.clear();
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
		return this.nettyHeaders.getDate();
	}

	@Override
	public Date getDateHeader(String name) throws ParseException {
		return this.nettyHeaders.getDateHeader(name);
	}

	@Override
	public String getHost() {
		return this.nettyHeaders.getHost();
	}

	@Override
	public String getHost(String defaultValue) {
		return this.nettyHeaders.getHost(defaultValue);
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
	public void remove(String name) {
		this.nettyHeaders.remove(name);
	}

	@Override
	public void removeTransferEncodingChunked() {
		this.nettyHeaders.removeTransferEncodingChunked();
	}

	@Override
	public void set(String name, String value) {
		this.nettyHeaders.set(name, value);
	}

	@Override
	public void set(String name, Iterable<String> values) {
		this.nettyHeaders.set(name, values);
	}

	@Override
	public void setContentLength(long length) {
		this.nettyHeaders.setContentLength(length);
	}

	@Override
	public void setDate(Date value) {
		this.nettyHeaders.setDate(value);
	}

	@Override
	public void setDateHeader(String name, Date value) {
		this.nettyHeaders.setDateHeader(name, value);
	}

	@Override
	public void setDateHeader(String name, Iterable<Date> values) {
		this.nettyHeaders.setDateHeader(name, values);
	}

	@Override
	public void setHost(String value) {
		this.nettyHeaders.setHost(value);
	}

	@Override
	public void setKeepAlive(boolean keepAlive) {
		this.nettyHeaders.setKeepAlive(keepAlive);
	}

	@Override
	public void setTransferEncodingChunked() {
		this.nettyHeaders.setTransferEncodingChunked();
	}

}
