package rxweb.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

/**
 * @author huangyong
 * @author zhangjessey
 */
public class Handler implements ServerHandler {
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
    public void handle(ServerRequest request, ServerResponse response) {
        DefaultHandlerInvoker defaultHandlerInvoker = new DefaultHandlerInvoker();
        try {
            Object o = defaultHandlerInvoker.invokeHandler(request, response, this);
            if (o instanceof String) {
                response.content(Observable.just(ByteBuffer.wrap(((String) o).getBytes(StandardCharsets.UTF_8))));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}