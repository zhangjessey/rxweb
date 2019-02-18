package rxweb.server;

import io.reactivex.netty.protocol.http.server.HttpServerRequest;

/**
 * @author huangyong
 * @author zhangjessey
 */
public interface HandlerInvoker {

    Object invokeHandler(HttpServerRequest request, Handler handler);
}
