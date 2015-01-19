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

package rxweb.converter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import reactor.io.buffer.Buffer;

import org.springframework.http.MediaType;
import org.springframework.util.Assert;

/**
 * @author Sebastien Deleuze
 */
public class StringConverter implements Converter<String> {

	private final Charset defaultEncoding = StandardCharsets.UTF_8;

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return String.class.isAssignableFrom(clazz);
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return String.class.isAssignableFrom(clazz);
	}

	@Override
	public <T extends String> T read(Class<T> type, Buffer buffer) {
		Assert.isTrue(type.equals(String.class));
		return (T) new String(buffer.asBytes(), defaultEncoding);
	}

	@Override
	public Buffer write(String s, MediaType contentType) {
		return Buffer.wrap(s.getBytes(defaultEncoding));
	}
}
