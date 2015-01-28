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

package rxweb.rx.reactor;

import java.nio.ByteBuffer;

import org.reactivestreams.Publisher;
import reactor.io.buffer.Buffer;
import reactor.rx.Streams;
import rxweb.http.Status;
import rxweb.http.Transfer;
import rxweb.server.ServerRequest;
import rxweb.server.ServerResponse;
import rxweb.server.ServerResponseHeaders;

/**
 * @author Sebastien Deleuze
 */
public class ReactorServerResponseAdapter implements ReactorServerResponse {

	private ServerResponse serverReponse;

	public ReactorServerResponseAdapter(ServerResponse serverReponse) {
		this.serverReponse = serverReponse;
	}

	@Override
	public ServerRequest getRequest() {
		return this.serverReponse.getRequest();
	}

	@Override
	public ReactorServerResponse status(Status status) {
		this.serverReponse.status(status);
		return this;
	}

	@Override
	public ServerResponseHeaders getHeaders() {
		return this.serverReponse.getHeaders();
	}

	@Override
	public ReactorServerResponse header(String name, String value) {
		this.serverReponse.header(name, value);
		return this;
	}

	@Override
	public ReactorServerResponse addHeader(String name, String value) {
		this.serverReponse.addHeader(name, value);
		return this;
	}

	@Override
	public ReactorServerResponse transfer(Transfer transfer) {
		this.serverReponse.transfer(transfer);
		return this;
	}

	@Override
	public ReactorServerResponse content(Publisher<Buffer> content) {
		this.serverReponse.content(Streams.create(content).map(buffer -> buffer.byteBuffer()));
		return this;
	}

	@Override
	public Publisher<Buffer> getContent() {
		return (Streams.create(this.serverReponse.getContent()).map(buffer -> new Buffer(buffer)));
	}

	@Override
	public boolean isStatusAndHeadersSent() {
		return this.serverReponse.isStatusAndHeadersSent();
	}

	@Override
	public void setStatusAndHeadersSent(boolean statusAndHeadersSent) {
		this.serverReponse.setStatusAndHeadersSent(statusAndHeadersSent);
	}

	@Override
	public Status getStatus() {
		return this.serverReponse.getStatus();
	}

	@Override
	public Transfer getTransfer() {
		return this.serverReponse.getTransfer();
	}
}
