package rxweb.server;

/**
 * @author huangyong
 * @author zhangjessey
 */
public interface HandlerInvoker {

    Object invokeHandler(ServerRequest request, ServerResponse response, Handler handler) throws Exception;
}
