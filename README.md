Implementation notes
====================

- No more artificial split between Handlers/Interceptors, just a handler chain
- 2 possible behaviors for streamed (chunked or SSE) transfer:
 - 1 chunk = 1 Object to decode/encode
 - Don't use chunks, just see a stream of data parsed with `JsonObjectDecoder`. More details
   on https://github.com/netty/netty/pull/2547
- The question to expose byte[] or a `ByteBuf` wrapper to avoid exposing Netty types, is not an easy question.
  Should the user or the framework be responsible to released the buffer ? For now I use byte[] to make things
  simpler. See these links for more details:
   - https://github.com/netty/netty/issues/3152
   - https://github.com/ReactiveX/RxNetty/issues/264
- Clear interface hierarchies for headers (Headers, RequestHeaders, ResponseHeaders)



