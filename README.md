Implementation notes
====================

- No more artificial split between Handlers/Interceptors, just a handler chain
- 2 possible behaviors for streamed (chunked or SSE) transfer:
 - 1 chunk = 1 Object to decode/encode
 - Don't use chunks, just see a stream of data parsed with `JsonObjectDecoder`. More details
   on https://github.com/netty/netty/pull/2547
- Need to create our own `ByteBuf`to avoid exposing Netty types
- Clear interface hierarchies for headers (Headers, RequestHeaders, ResponseHeaders)


