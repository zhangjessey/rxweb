Implementation notes
====================

**This is an early draft prototype, nothing to use for now **

- See TODO Javadoc comments for in code remarks
- Need to work on converters ...
- No more split between `Handler`/`Interceptor`, just a handler chain.
- Since handlers are asynchronous, they are executed sequentially in a non blocking way by calling context.next()
- We should manage HTTP Request and Response Lifecycle, in order for example to know if
  headers have already been written and so can't be modified in the response, etc.
- I have tried to have a fluent API with no builder (like `ClientRequest`). Not 100% sure it is the right path, it is just a try.
- `ServerResponse.transfer()` allows to choose between normal, chunked or SSE modes.
- I have tried to provide a clear interface hierarchies for headers
- Without Java 8 this kind of API does not make sense, so using Java 8 minimal requirement
- We will have 2 possible behaviors to implement for streamed (chunked or SSE) transfers:
 - 1 chunk = 1 Object to decode/encode
 - Don't use chunks, just see a stream of data parsed with `JsonObjectDecoder` [1]
- What type should we use for byte buffer ? Should the user or the framework be responsible to released the buffer ?
  See these links for more details [2][3]. For now I use Reactor Buffer class since we already expose Reactor type
  in our API.
- Client require a small footprint and may be used in non Spring context. So should we make
  the dependency on spring-core Mandatory ? We just use a small subset so maybe we could build
  a smaller version with just the classes we use as described in this SPR-10258 comment [4].
- I have chosen to have no dependency on spring-web (too much overlap between the old and new implementation)
- What about using this prototype to create a reactor-http module as proposed by Stéphane?
- We reuse Reactor Netty support [5]
- For non blocking callback we have the choice between Promise style API (with onSuccess/onError) like done in the Client
  class and callback style API (here we don't need onError so just a callback seems to be the right choice) like done in
  the Server class.
- On API POV, we may extend Promise and Stream classes to add our own listener: onStatus, onHeaders, ...
- Jetty client Async API [6] is interesting, it provides a lot of fine grained hooks. In current implementation
  I have chosen a simplest path: provide a hook when the headers has been received, it will cover most cases.


- [1] https://github.com/netty/netty/pull/2547
- [2] https://github.com/netty/netty/issues/3152
- [3] https://github.com/ReactiveX/RxNetty/issues/264
- [4] https://jira.spring.io/browse/SPR-10258?focusedCommentId=110154
- [5] https://github.com/reactor/reactor/tree/master/reactor-net/src/main/java/reactor/io/net/netty
- [6] http://www.eclipse.org/jetty/documentation/current/http-client-api.html#http-client-async
