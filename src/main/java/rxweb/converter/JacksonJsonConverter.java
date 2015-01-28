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

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.io.buffer.Buffer;
import rxweb.http.MediaType;

/**
 * @author Sebastien Deleuze
 */
public class JacksonJsonConverter implements Converter<Object> {

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	@Override
	public <T extends Object> T read(Class<T> type, Buffer buffer) {
		try {
			return this.mapper.readValue(buffer.asBytes(), type);
		}
		catch (IOException e) {
			throw new ConversionException(e);
		}
	}

	@Override
	public Buffer write(Object o, MediaType contentType) {
		try {
			return Buffer.wrap(this.mapper.writeValueAsBytes(o));
		}
		catch (JsonProcessingException e) {
			throw new ConversionException(e);
		}
	}
}
