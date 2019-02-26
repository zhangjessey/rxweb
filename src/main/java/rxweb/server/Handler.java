package rxweb.server;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import rx.Observable;
import rxweb.bean.WebRequest;
import rxweb.support.DefaultConverter;

import java.lang.reflect.Method;

/**
 * 实际的请求处理器，对应一个注解方式的方法或者函数式路由的函数式接口
 *
 * @author huangyong
 * @author zhangjessey
 */
public class Handler implements WebRequestHandler<ByteBuf, ByteBuf> {

    private Class<?> actionClass;
    private Method actionMethod;

    private Class<?> requestBodyClass;
    private String requestBody;


    public Handler(Class<?> actionClass, Method actionMethod, Class<?> requestBodyClass) {
        this.actionClass = actionClass;
        this.actionMethod = actionMethod;
        this.requestBodyClass = requestBodyClass;
    }

    public Class<?> getActionClass() {
        return actionClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }



    public Class<?> getRequestBodyClass() {
        return requestBodyClass;
    }

    public void setRequestBodyClass(Class<?> requestBodyClass) {
        this.requestBodyClass = requestBodyClass;
    }

    @Override
    public Observable<Void> handle(WebRequest<ByteBuf> webRequest, HttpServerResponse<ByteBuf> response) {

        DefaultHandlerInvoker defaultHandlerInvoker = new DefaultHandlerInvoker();
        Observable<?> obs = defaultHandlerInvoker.invokeHandler(webRequest, this, response);
        DefaultConverter defaultConverter = new DefaultConverter();

        return response.writeString(obs.map(o -> {
            if (o instanceof String) {
                return (String) o;
            } else {
                return defaultConverter.serialize(o);
            }

        }));
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}