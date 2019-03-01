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
     * @param request 内部请求对象
     * @param handler 将被调用的Handler
     * @param response RxNetty原生的response
     * @return 调用结束后的返回值
     */
    Object invokeHandler(WebRequest request, Handler handler, HttpServerResponse<ByteBuf> response);
}
