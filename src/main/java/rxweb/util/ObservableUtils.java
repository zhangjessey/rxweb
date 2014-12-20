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

import java.util.List;
import java.util.concurrent.CompletableFuture;

import rx.Observable;

/**
 * @author Sebastien Deleuze
 * @see <a href="http://www.nurkiewicz.com/2014/11/converting-between-completablefuture.html">Converting between Completablefuture and Observable</a>
 */
public class ObservableUtils {

	public static <T> Observable<T> toObservable(CompletableFuture<T> future) {
		return Observable.create(subscriber -> future.whenComplete((result, error) -> {
			if (error != null) {
				subscriber.onError(error);
			}
			else {
				subscriber.onNext(result);
				subscriber.onCompleted();
			}
		}));
	}

	public static <T> CompletableFuture<List<T>> fromObservable(Observable<T> observable) {
		final CompletableFuture<List<T>> future = new CompletableFuture<>();
		observable
				.doOnError(future::completeExceptionally)
				.toList()
				.forEach(future::complete);
		return future;
	}

	public static CompletableFuture<Void> fromVoidObservable(Observable<Void> observable) {
		final CompletableFuture<Void> future = new CompletableFuture<>();
		observable
				.doOnError(future::completeExceptionally)
				.toList().map(voids -> (Void)null)
				.single().forEach(future::complete);
		return future;
	}

	public static <T> CompletableFuture<T> fromSingleObservable(Observable<T> observable) {
		final CompletableFuture<T> future = new CompletableFuture<>();
		observable
				.doOnError(future::completeExceptionally)
				.single()
				.forEach(future::complete);
		return future;
	}

}
