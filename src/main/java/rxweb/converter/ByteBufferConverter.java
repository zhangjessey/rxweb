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

import java.nio.ByteBuffer;

import reactor.io.buffer.Buffer;
import rxweb.http.MediaType;
import rxweb.support.Assert;

/**
 * @author Sebastien Deleuze
 */
public class ByteBufferConverter implements Converter<ByteBuffer> {

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return ByteBuffer.class.isAssignableFrom(clazz);
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return ByteBuffer.class.isAssignableFrom(clazz);
	}

	@Override
	public <T extends ByteBuffer> T read(Class<T> type, Buffer buffer) {
		Assert.isTrue(type.equals(ByteBuffer.class));
		return (T) buffer.byteBuffer();
	}

	@Override
	public Buffer write(ByteBuffer buffer, MediaType contentType) {
		Assert.isAssignable(ByteBuffer.class, buffer.getClass());
		return new Buffer(buffer);
	}
}
