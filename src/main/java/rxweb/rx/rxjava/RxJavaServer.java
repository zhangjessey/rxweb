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

/**
 * @author Sebastien Deleuze
 */
public interface RxJavaServer {

	/** Complete when the server is started **/
	Observable<Void> start();

	/** Complete when the server is stopped **/
	Observable<Void> stop();

	void get(final String path, final RxJavaServerHandler handler);

	void post(final String path, final RxJavaServerHandler handler);

	void put(final String path, final RxJavaServerHandler handler);

	void delete(final String path, final RxJavaServerHandler handler);

}
