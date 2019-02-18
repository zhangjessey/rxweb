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

package rxweb.client;

import rxweb.support.LinkedCaseInsensitiveMap;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Sebastien Deleuze
 */
public class DefaultClientRequestHeaders implements ClientRequestHeaders {

	private final Map<String, List<String>>
			headers = new LinkedCaseInsensitiveMap<List<String>>(8, Locale.ENGLISH);

	@Override
	public ClientRequestHeaders add(String name, String value) {
		List<String> values = headers.get(name);
		if (values == null) {
			values = new LinkedList<String>();
			this.headers.put(name, values);
		}
		values.add(value);
		return this;
	}

	@Override
	public ClientRequestHeaders add(String name, Iterable<String> values) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHeaders addDateHeader(String name, Date value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHeaders clear() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public boolean contains(String name) {
		return this.headers.containsKey(name);
	}

	@Override
	public boolean contains(String name, String value, boolean ignoreCaseValue) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<Map.Entry<String, String>> entries() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public String get(String name) {
		List<String> values = headers.get(name);
		return values != null ? values.get(0) : null;
	}

	@Override
	public List<String> getAll(String name) {
		return this.headers.get(name);
	}

	@Override
	public Date getDate() throws ParseException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Date getDateHeader(String name) throws ParseException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public String getHost() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public boolean isEmpty() {
		return this.headers.isEmpty();
	}

	@Override
	public Set<String> names() {
		return this.headers.keySet();
	}

	@Override
	public ClientRequestHeaders remove(String name) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHeaders removeTransferEncodingChunked() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHeaders set(String name, String value) {
		List<String> values = new LinkedList<String>();
		values.add(value);
		headers.put(name, values);
		return this;
	}

	@Override
	public ClientRequestHeaders set(String name, Iterable<String> values) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHeaders contentLength(long length) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHeaders date(Date value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHeaders dateHeader(String name, Date value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHeaders dateHeader(String name, Iterable<Date> values) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHeaders host(String value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHeaders keepAlive(boolean keepAlive) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ClientRequestHeaders transferEncodingChunked() {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
