package rxweb.server;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import rx.Observable;
import rxweb.bean.WebRequest;

/**
 * 专门负责404的处理器
 *
 * @author zhangjessey
 */
public class NotFoundHandler implements WebRequestHandler<ByteBuf, ByteBuf> {


    @Override
    public Observable<Void> handle(WebRequest<ByteBuf> webRequest, HttpServerResponse<ByteBuf> response) {
        response.setStatus(HttpResponseStatus.NOT_FOUND);
        return Observable.empty();
    }
}
