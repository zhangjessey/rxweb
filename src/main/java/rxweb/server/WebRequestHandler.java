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

    Observable<Void> handle(WebRequest<I> webRequest, HttpServerResponse<O> response);


}
