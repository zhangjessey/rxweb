package rxweb.server;

import io.reactivex.netty.protocol.http.server.HttpServerRequest;

/**
 * 方法调用器
 *
 * @author huangyong
 * @author zhangjessey
 */
public interface HandlerInvoker {

    /**
     * 根据一个HttpServerRequest调用对应的Handler
     */
    Object invokeHandler(HttpServerRequest request, Handler handler);
}
