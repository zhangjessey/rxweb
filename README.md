Implementation notes
====================

This is a prototype of a Web Framework based on a Reactor 2.0 core with both Netty and Servlet 3.1 (TODO) engines.
A Reactive Streams SPI is exposed (see [ServerTests.java](https://github.com/sdeleuze/spring-rxweb/blob/master/src/test/java/rxweb/ServerTests.java))
and both Reactor 2.0 (see [ReactorServerTests.java](https://github.com/sdeleuze/spring-rxweb/blob/master/src/test/java/rxweb/ReactorServerTests.java))
and RxJava 1.0 (see [RxJavaServerTests.java](https://github.com/sdeleuze/spring-rxweb/blob/master/src/test/java/rxweb/RxJavaServerTests.java)) API are provided.

- See TODO Javadoc comments for in code remarks
- No more split between `Handler`/`Interceptor`, just a collection of handlers that could be run in order or at the same time.
- `ServerResponse.transfer()` allows to choose between normal, chunked or SSE modes.
- I have tried to provide a clear interface hierarchies for headers
- Without Java 8 this kind of API does not make sense, so using Java 8 minimal requirement
- We will have 2 possible behaviors to implement for streamed (chunked or SSE) transfers:
 - 1 chunk = 1 Object to decode/encode
 - Don't use chunks, just see a stream of data parsed with `JsonObjectDecoder` [1]
- Client require a small footprint and may be used in non Spring context. So should we make
  the dependency on spring-core Mandatory ? We just use a small subset so maybe we could build
  a smaller version with just the classes we use as described in this SPR-10258 comment [4].
- We reuse Reactor Netty TCP support [5]
- For non blocking callback we have the choice between Promise style API (with onSuccess/onError) like done in the Client
  class and callback style API (here we don't need onError so just a callback seems to be the right choice) like done in
  the Server class.
- Jetty client Async API [6] is interesting, it provides a lot of fine grained hooks. In current implementation
  I have chosen a simplest path: provide a hook when the headers has been received, it will cover most cases.


- [1] https://github.com/netty/netty/pull/2547
- [2] https://github.com/netty/netty/issues/3152
- [3] https://github.com/ReactiveX/RxNetty/issues/264
- [4] https://jira.spring.io/browse/SPR-10258?focusedCommentId=110154
- [5] https://github.com/reactor/reactor/tree/master/reactor-net/src/main/java/reactor/io/net/netty
- [6] http://www.eclipse.org/jetty/documentation/current/http-client-api.html#http-client-async
