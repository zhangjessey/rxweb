package rxweb.engine.server.netty;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import rx.Observable;
import rxweb.bean.WebRequest;
import rxweb.mapping.HandlerResolver;
import rxweb.server.DefaultHandlerResolver;
import rxweb.server.Handler;
import rxweb.server.NotFoundHandler;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 转发器，负责请求转发处理返回
 * @author gt_tech
 * @author zhangjessey
 */
public class Dispatcher implements RequestHandler<ByteBuf, ByteBuf> {

    private HandlerResolver handlerResolver = DefaultHandlerResolver.getSingleton();

    private final String EMPTY_STRING = "";

    @Override
    public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {

        WebRequest<String> wreq = new WebRequest<>(request);


        return Observable.defer(() -> request.getContent()).<String>map(bf -> bf.toString(Charset.defaultCharset())).reduce(EMPTY_STRING, (acc, value) -> acc.concat(value)).firstOrDefault(EMPTY_STRING).map(strRequestContent -> {
            if (strRequestContent != null && !strRequestContent.equals(EMPTY_STRING)) {
                if ("application/json".equals(wreq.getRequestContentType())) {
                    wreq.setBody(strRequestContent);
                }
            }
            return wreq;
        }).map(webRequest -> {
            List<RequestHandler<ByteBuf, ByteBuf>> resolve = handlerResolver.resolve(webRequest);
            if (resolve.isEmpty()) {
                return new NotFoundHandler();
            }

            RequestHandler rh = resolve.get(0);
            if (rh instanceof Handler) {
                ((Handler) rh).setRequestBody(webRequest.getBody());
            }

            return rh;
        }).flatMap(requestHandler -> requestHandler.handle(request, response));
    }
}
