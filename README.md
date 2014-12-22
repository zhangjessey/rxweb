Implementation notes
====================

**This is an early draft prototype, nothing to use for now.**

- No more split between `Handler`/`Interceptor`, just a handler chain
- Try to have a fluent API with no builder (like `ClientRequest`)
- `ServerResponse.transfer()` allows to choose between normal, chunked or SSE modes.
- Clear interface hierarchies for headers
- Without Java 8 this kind of API does not make sense, so using Java 8 minimal requirement
- We will have 2 possible behaviors to implement for streamed (chunked or SSE) transfers:
 - 1 chunk = 1 Object to decode/encode
 - Don't use chunks, just see a stream of data parsed with `JsonObjectDecoder` [1]
- The question to expose byte[] or a `ByteBuf` wrapper to avoid exposing Netty types, is not an easy question.
  Should the user or the framework be responsible to released the buffer ? For now I use byte[] to make things
  simpler. See these links for more details [2][3].
- Using CompletableFuture since RxJava 1.0 has no Future (single value emitted) type, but RxJava 2.0
  may have it [4]
- RxJava 1.0 has no support for Java 8 streams, but RxJava 2.0 will have it [5]
- RxJava is not natively compliant with ReactiveStreams, we need to use an extension [6]


- [1] https://github.com/netty/netty/pull/2547
- [2] https://github.com/netty/netty/issues/3152
- [3] https://github.com/ReactiveX/RxNetty/issues/264
- [4] https://github.com/ReactiveX/RxJava/issues/1595
- [5] https://github.com/ReactiveX/RxJava/issues/1596
- [6] https://github.com/ReactiveX/RxJavaReactiveStreams


