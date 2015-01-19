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

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * @author Sebastien Deleuze
 */
public class NettyRequestHeadersAdapter extends org.springframework.http.HttpHeaders {

	private final HttpRequest nettyRequest;
	private final HttpHeaders nettyHeaders;

	public NettyRequestHeadersAdapter(HttpRequest nettyRequest) {
		this.nettyRequest = nettyRequest;
		this.nettyHeaders = nettyRequest.headers();
	}

	@Override
	public void setAccept(List<MediaType> acceptableMediaTypes) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<MediaType> getAccept() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setAcceptCharset(List<Charset> acceptableCharsets) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<Charset> getAcceptCharset() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setAllow(Set<HttpMethod> allowedMethods) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Set<HttpMethod> getAllow() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setCacheControl(String cacheControl) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public String getCacheControl() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setConnection(String connection) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setConnection(List<String> connection) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<String> getConnection() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setContentDispositionFormData(String name, String filename) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setContentLength(long contentLength) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public long getContentLength() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setContentType(MediaType mediaType) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public MediaType getContentType() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setDate(long date) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public long getDate() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setETag(String eTag) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public String getETag() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setExpires(long expires) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public long getExpires() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setIfModifiedSince(long ifModifiedSince) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	@Deprecated
	public long getIfNotModifiedSince() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public long getIfModifiedSince() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setIfNoneMatch(String ifNoneMatch) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setIfNoneMatch(List<String> ifNoneMatchList) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	protected String toCommaDelimitedString(List<String> list) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<String> getIfNoneMatch() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	protected List<String> getFirstValueAsList(String header) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setLastModified(long lastModified) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public long getLastModified() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setLocation(URI location) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public URI getLocation() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setOrigin(String origin) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public String getOrigin() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setPragma(String pragma) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public String getPragma() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setUpgrade(String upgrade) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public String getUpgrade() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public long getFirstDate(String headerName) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setDate(String headerName, long date) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public String getFirst(String headerName) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void add(String headerName, String headerValue) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void set(String headerName, String headerValue) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void setAll(Map<String, String> values) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Map<String, String> toSingleValueMap() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public boolean containsKey(Object key) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<String> get(Object key) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<String> put(String key, List<String> value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<String> remove(Object key) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void putAll(Map<? extends String, ? extends List<String>> map) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Set<String> keySet() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Collection<List<String>> values() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Set<Entry<String, List<String>>> entrySet() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public boolean equals(Object other) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<String> getOrDefault(Object key, List<String> defaultValue) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void forEach(BiConsumer<? super String, ? super List<String>> action) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void replaceAll(BiFunction<? super String, ? super List<String>, ? extends List<String>> function) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<String> putIfAbsent(String key, List<String> value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public boolean remove(Object key, Object value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public boolean replace(String key, List<String> oldValue, List<String> newValue) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<String> replace(String key, List<String> value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<String> computeIfAbsent(String key, Function<? super String, ? extends List<String>> mappingFunction) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<String> computeIfPresent(String key, BiFunction<? super String, ? super List<String>, ? extends List<String>> remappingFunction) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<String> compute(String key, BiFunction<? super String, ? super List<String>, ? extends List<String>> remappingFunction) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<String> merge(String key, List<String> value, BiFunction<? super List<String>, ? super List<String>, ? extends List<String>> remappingFunction) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public boolean isEmpty() {
		return this.nettyHeaders.isEmpty();
	}

}
