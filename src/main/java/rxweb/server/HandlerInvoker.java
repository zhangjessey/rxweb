package rxweb.server;

import io.reactivex.netty.protocol.http.server.HttpServerRequest;

/**
 * 方法调用器
 * @author huangyong
 * @author zhangjessey
 */
public interface HandlerInvoker {

    Object invokeHandler(HttpServerRequest request, Handler handler);
}
