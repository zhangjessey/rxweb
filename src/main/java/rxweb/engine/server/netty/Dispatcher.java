package rxweb.engine.server.netty;

import com.google.common.collect.Multimap;
import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import rx.Observable;
import rxweb.bean.WebRequest;
import rxweb.mapping.Condition;
import rxweb.mapping.HandlerResolver;
import rxweb.server.BootstrapConfig;
import rxweb.server.DefaultHandlerInvoker;
import rxweb.server.DefaultHandlerResolver;
import rxweb.server.Handler;
import rxweb.server.NotFoundHandler;
import rxweb.server.WebRequestHandler;
import static rxweb.support.Constants.JSON;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 转发器，负责请求转发处理返回
 *
 * @author gt_tech
 * @author zhangjessey
 */
public class Dispatcher implements RequestHandler<ByteBuf, ByteBuf> {

    private HandlerResolver handlerResolver = DefaultHandlerResolver.getSingleton();

    private final String EMPTY_STRING = "";

    @Override
    @SuppressWarnings("unchecked")
    public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {

        WebRequest<String> wreq = new WebRequest<>(request);
        DefaultHandlerInvoker defaultHandlerInvoker = new DefaultHandlerInvoker();

        return Observable.defer(() -> request.getContent()).<String>map(bf -> bf.toString(Charset.defaultCharset())).reduce(EMPTY_STRING, (acc, value) -> acc.concat(value)).firstOrDefault(EMPTY_STRING).map(strRequestContent -> {
            if (strRequestContent != null && !strRequestContent.equals(EMPTY_STRING)) {
                if (JSON.equals(wreq.getRequestContentType())) {
                    wreq.setBody(strRequestContent);
                }
            }
            return wreq;
        }).map(stringWebRequest -> {
            for (Map.Entry<Condition<HttpServerRequest>, WebRequestHandler<ByteBuf, ByteBuf>> entry : BootstrapConfig.CONDITION_REQUEST_HANDLER_MAP.entrySet()) {
                String path = entry.getKey().getUrl();
                if (path.matches(".+\\{\\w+}.*")) {
                    // 将请求路径中的占位符 {\w+} 转换为正则表达式 (\\w+)
                    path = path.replaceAll("\\{\\w+}", "(\\\\w+)");
                }
                String decodedPath = stringWebRequest.getHttpServerRequest().getDecodedPath();
                Matcher matcher = Pattern.compile(path).matcher(decodedPath);
                if (entry.getKey().getHttpMethod().equals(stringWebRequest.getHttpServerRequest().getHttpMethod()) && matcher.matches()) {
                    stringWebRequest.setRequestPathMatcher(matcher);
                    stringWebRequest.setWebRequestHandler(entry.getValue());
                    int i = matcher.groupCount();
                    List<Class<?>> collect = IntStream.rangeClosed(1, i).mapToObj(ii -> String.class).collect(Collectors.toList());
                    Multimap<Class<?>, Object> pathParamList = defaultHandlerInvoker.createPathParamList(stringWebRequest.getRequestPathMatcher(), collect);
                    List<Object> objects = (List<Object>) pathParamList.get(String.class);
                    stringWebRequest.setUrlParams(objects);
                    return stringWebRequest;
                }

            }
            return stringWebRequest;
        }).map(webRequest -> {
            List<WebRequestHandler<ByteBuf, ByteBuf>> resolve = handlerResolver.resolve(webRequest);
            if (resolve.isEmpty()) {
                return new NotFoundHandler();
            }

            WebRequestHandler rh = resolve.get(0);
            if (rh instanceof Handler) {
                ((Handler) rh).setRequestBody(webRequest.getBody());
            }

            return rh;
        }).flatMap(requestHandler -> requestHandler.handle(wreq, response));
    }
}
