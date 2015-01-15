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

import org.reactivestreams.Publisher;
import rxweb.client.ClientRequest;
import rxweb.client.ClientRequestHolder;
import rxweb.client.ClientResponse;

/**
 * @author Sebastien Deleuze
 */
public interface Client {

	// Return the client response as soon as headers has been received (we don't wait to have received the full body).
	Publisher<ClientResponse> execute(final ClientRequest request);

	ClientRequestHolder get(final String uri);

	ClientRequestHolder post(final String uri);

	ClientRequestHolder put(final String uri);

	ClientRequestHolder delete(final String uri);

}
