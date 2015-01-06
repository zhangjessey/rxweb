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

package rxweb.util;

import reactor.rx.Promise;
import rx.Observable;

/**
 * @author Sebastien Deleuze
 * @see <a href="http://www.nurkiewicz.com/2014/11/converting-between-completablefuture.html">Converting between Completablefuture and Observable</a>
 */
public class ObservableUtils {

	public static <T> Observable<T> toObservable(Promise<T> promise) {
		return Observable.create(subscriber -> {
			promise.onSuccess(result -> {
				subscriber.onNext(result);
				subscriber.onCompleted();
			});
			promise.onError(subscriber::onError);
		});
	}

	public static Promise<Void> fromVoidObservable(Observable<Void> observable) {
		final Promise<Void> promise = new Promise<>();
		observable
				.doOnError(promise::onError)
				.toList().map(voids -> (Void)null)
				.single().forEach(promise::onNext);
		return promise;
	}

	public static <T> Promise<T> fromSingleObservable(Observable<T> observable) {
		final Promise<T> promise = new Promise<>();
		observable
				.doOnError(promise::onError)
				.single()
				.forEach(promise::onNext);
		return promise;
	}

}
