package rxweb.server;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import rxweb.bean.WebRequest;

/**
 * Handler调用器接口
 *
 * @author zhangjessey
 */
public interface WebInvoker {
    /**
     * 调用具体Handler
     */
    Object invokeHandler(WebRequest request, Handler handler, HttpServerResponse<ByteBuf> response);
}
