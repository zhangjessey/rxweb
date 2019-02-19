package rxweb.server;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rxweb.support.DefaultConverter;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

/**
 * @author huangyong
 * @author zhangjessey
 */
public class Handler implements RequestHandler<ByteBuf, ByteBuf> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Class<?> actionClass;
    private Method actionMethod;
    private Matcher requestPathMatcher;

    public Handler(Class<?> actionClass, Method actionMethod) {
        this.actionClass = actionClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getActionClass() {
        return actionClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public Matcher getRequestPathMatcher() {
        return requestPathMatcher;
    }

    public void setRequestPathMatcher(Matcher requestPathMatcher) {
        this.requestPathMatcher = requestPathMatcher;
    }


    @Override
    public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
        DefaultHandlerInvoker defaultHandlerInvoker = new DefaultHandlerInvoker();
        DefaultConverter defaultConverter = new DefaultConverter();
        Observable<?> obs = defaultHandlerInvoker.invokeHandler(request, this);

        return response.writeString(obs.map(o -> {
            if (o instanceof String) {
                return (String) o;
            } else {
                return defaultConverter.serialize(o);
            }

        }));

    }
}