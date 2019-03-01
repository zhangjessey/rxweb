package rxweb.server;

import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import rx.Observable;
import rxweb.bean.WebRequest;

/**
 * 自定义handler接口
 *
 * @author zhangjessey
 */
public interface WebRequestHandler<I, O> {

    /**
     * 实际的处理过程
     *
     * @param webRequest 内部请求对象
     * @param response   RxNetty原生的response
     * @return 实际返回为空，可以理解为无作用，实际在response返回
     */
    Observable<Void> handle(WebRequest<I> webRequest, HttpServerResponse<O> response);


}
