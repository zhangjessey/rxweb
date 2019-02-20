package rxweb.engine.server.netty;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rxweb.mapping.HandlerResolver;
import rxweb.server.DefaultHandlerResolver;
import rxweb.server.NotFoundHandler;

/**
 * @author zhangjessey
 */
public class Dispatcher implements RequestHandler<ByteBuf, ByteBuf> {

    private HandlerResolver handlerResolver = DefaultHandlerResolver.getSingleton();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {

        return Observable.from(handlerResolver.resolve(request)).
                firstOrDefault(new NotFoundHandler(), handler -> true).flatMap(requestHandler -> requestHandler.handle(request, response));

    }
}
