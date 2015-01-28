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

package rxweb.rx.rxjava;

import rx.Observable;
import rx.RxReactiveStreams;
import rxweb.http.Transfer;
import rxweb.server.ServerRequest;
import rxweb.server.ServerResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * @author Sebastien Deleuze
 */
public class RxJavaServerResponseAdapter implements RxJavaServerResponse {

	private ServerResponse serverReponse;

	public RxJavaServerResponseAdapter(ServerResponse serverReponse) {
		this.serverReponse = serverReponse;
	}

	@Override
	public ServerRequest getRequest() {
		return this.serverReponse.getRequest();
	}

	@Override
	public RxJavaServerResponse status(HttpStatus status) {
		this.serverReponse.status(status);
		return this;
	}

	@Override
	public HttpHeaders getHeaders() {
		return this.serverReponse.getHeaders();
	}

	@Override
	public RxJavaServerResponse header(String name, String value) {
		this.serverReponse.header(name, value);
		return this;
	}

	@Override
	public RxJavaServerResponse addHeader(String name, String value) {
		this.serverReponse.addHeader(name, value);
		return this;
	}

	@Override
	public RxJavaServerResponse transfer(Transfer transfer) {
		this.serverReponse.transfer(transfer);
		return this;
	}

	@Override
	public RxJavaServerResponse content(Observable<?> content) {
		this.serverReponse.content(RxReactiveStreams.toPublisher(content));
		return this;
	}

	@Override
	public Observable<?> getContent() {
		return RxReactiveStreams.toObservable(this.serverReponse.getContent());
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
	public HttpStatus getStatus() {
		return this.serverReponse.getStatus();
	}

	@Override
	public Transfer getTransfer() {
		return this.serverReponse.getTransfer();
	}
}
