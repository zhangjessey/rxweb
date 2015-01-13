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

package rxweb.server;

import reactor.fn.Function;
import reactor.io.buffer.Buffer;
import reactor.rx.Stream;
import rxweb.http.Request;

/**
 * @author Sebastien Deleuze
 */
public interface ServerRequest extends Request {

	ServerRequestHeaders getHeaders();

	Stream<Buffer> getRawContentStream();

	void setConvert(Function<Buffer, ?> convert);

	/** For the moment, {@link Buffer} and {@link String} (UTF-8) are supported
	 */
	Stream<?> getContentStream();

}
