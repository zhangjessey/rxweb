package rxweb.engine.server.netty;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rxweb.bean.WebRequest;
import rxweb.mapping.HandlerResolver;
import rxweb.server.DefaultHandlerResolver;
import rxweb.server.Handler;
import rxweb.server.NotFoundHandler;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author gt_tech
 * @author zhangjessey
 */
public class Dispatcher implements RequestHandler<ByteBuf, ByteBuf> {

    private HandlerResolver handlerResolver = DefaultHandlerResolver.getSingleton();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    final String EMPTY_STRING = "";

    @Override
    public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {

        WebRequest<String> wreq = new WebRequest<>(request);


        return Observable.defer(() -> request.getContent()).<String>map(bf -> bf.toString(Charset.defaultCharset())).reduce(EMPTY_STRING, (acc, value) -> new StringBuilder(acc).append(value).toString()).firstOrDefault(EMPTY_STRING).map(strRequestContent -> {
            if (strRequestContent != null && !strRequestContent.equals(EMPTY_STRING)) {
                if (wreq.getRequestContentType().equals("application/json")) {
                    wreq.setBody(strRequestContent);
                }
            }
            return wreq;
        }).map(WebRequest -> {
            List<RequestHandler> resolve = handlerResolver.resolve(WebRequest);
            if (resolve.isEmpty()) {
                return new NotFoundHandler();
            }
            Handler requestHandler = (Handler) resolve.get(0);

            requestHandler.setRequestBody(WebRequest.getBody());
            return requestHandler;
        }).flatMap(requestHandler -> requestHandler.handle(request, response));
    }
}
