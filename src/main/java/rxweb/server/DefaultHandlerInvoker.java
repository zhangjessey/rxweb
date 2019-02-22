package rxweb.server;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rxweb.bean.Params;
import rxweb.support.DefaultConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 默认的HandlerInvoker
 *
 * @author huangyong
 * @author zhangjessey
 */
public class DefaultHandlerInvoker implements HandlerInvoker {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Observable<?> invokeHandler(HttpServerRequest request, Handler handler) {
        try {
            // 获取 Controller 相关信息
            Class<?> actionClass = handler.getActionClass();
            Method actionMethod = handler.getActionMethod();
            // 创建 Controller 实例
            Object actionInstance = BootstrapConfig.CONTROLLER_CLASS_OBJECT_MAP.get(actionClass);
            // 创建 Controller 方法的参数列表
            Multimap<Class<?>, Object> actionMethodParamList = createActionMethodParamList(request, handler);
            // 检查参数列表是否合法,不合法则使其合法
            List<Object> realParamList = getRealParamList(actionMethod, actionMethodParamList);
            // 调用 Controller 方法
            return invokeActionMethod(actionMethod, actionInstance, realParamList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

    }

    @SuppressWarnings("unchecked")
    private Multimap<Class<?>, Object> createActionMethodParamList(HttpServerRequest request, Handler handler) {

        Multimap<Class<?>, Object> pathParamMap = LinkedListMultimap.create();
        // 获取Controller方法参数类型
        Class<?>[] actionParamTypes = handler.getActionMethod().getParameterTypes();
        // 添加路径参数列表（请求路径中的带占位符参数）
        if (handler.getRequestPathMatcher() != null) {
            List<Class<?>> collect = Arrays.stream(actionParamTypes).filter(aClass -> !aClass.isAssignableFrom(Params.class)).collect(Collectors.toList());
            //Class<?>[] pathParamList = collect.toArray(new Class<?>[]{});
            pathParamMap = createPathParamList(handler.getRequestPathMatcher(), collect);
        }
        //获取普通请求参数列表
        Multimap<Class<?>, Object> requestParamMap = getRequestParamMap(request);
        pathParamMap.putAll(requestParamMap);

        if (handler.getRequestBody() != null && handler.getRequestBodyClass() != null) {
            DefaultConverter defaultConverter = new DefaultConverter();

            //获取requestbody参数
            Object deserialize = defaultConverter.deserialize((String) handler.getRequestBody(), handler.getRequestBodyClass());
            pathParamMap.put(handler.getRequestBodyClass(), deserialize);
        }

        // 返回参数列表
        return pathParamMap;
    }

    /**
     * 获取普通请求参数列表（包括 Query String ，暂不包含 Form Data,multidata）
     */
    private Multimap<Class<?>, Object> getRequestParamMap(HttpServerRequest<ByteBuf> request) {

        Multimap<Class<?>, Object> just = LinkedListMultimap.create(1);

        Map<String, Object> collect = request.getQueryParameters().entrySet().stream().map(stringListEntry -> Maps.immutableEntry(stringListEntry.getKey(), stringListEntry.getValue().get(stringListEntry.getValue().size() - 1))).
                collect(Collectors.toMap((Map.Entry o) -> (String) o.getKey(), Map.Entry::getValue));

        just.put(Params.class, new Params(collect));

        return just;

    }

    private Multimap<Class<?>, Object> createPathParamList(Matcher requestPathMatcher, List<Class<?>> actionParamTypes) {

        Multimap<Class<?>, Object> multiMap = LinkedListMultimap.create();

        IntStream.rangeClosed(1, requestPathMatcher.groupCount()).forEach(i -> {
            String param = requestPathMatcher.group(i);
            //Class<?> paramType = actionParamTypes[i - 1];
            for (Class<?> paramType : actionParamTypes) {
                if (paramType.equals(int.class) || paramType.equals(Integer.class)) {
                    multiMap.put(int.class, Integer.valueOf(param));
                    actionParamTypes.remove(paramType);
                    break;
                } else if (paramType.equals(long.class) || paramType.equals(Long.class)) {
                    multiMap.put(long.class, Long.valueOf(param));
                    actionParamTypes.remove(paramType);
                    break;
                } else if (paramType.equals(double.class) || paramType.equals(Double.class)) {
                    multiMap.put(double.class, Double.valueOf(param));
                    actionParamTypes.remove(paramType);
                    break;
                } else if (paramType.equals(String.class)) {
                    multiMap.put(String.class, param);
                    actionParamTypes.remove(paramType);
                    break;
                }
            }

        });
        return multiMap;
    }

    private Observable<?> invokeActionMethod(Method actionMethod, Object actionInstance, List<Object> actionMethodParamList) throws IllegalAccessException, InvocationTargetException {
        // 通过反射调用 Controller 方法
        // 取消类型安全检测（可提高反射性能）
        actionMethod.setAccessible(true);
        Object invoke = actionMethod.invoke(actionInstance, actionMethodParamList.toArray());
        return (Observable<?>) invoke;
    }

    private List<Object> getRealParamList(Method actionMethod, Multimap<Class<?>, Object> actionMethodParamList) {
        List<Object> realParams = new ArrayList<>();
        // 判断 Controller 方法参数的个数是否匹配
        Class<?>[] actionMethodParameterTypes = actionMethod.getParameterTypes();
        int pathLength = actionMethodParameterTypes.length;
        if (pathLength == 0) {
            actionMethodParamList.clear();
            return Collections.emptyList();
        }
        //注意：同类型按顺序匹配
        for (Class<?> actionMethodParameterType : actionMethodParameterTypes) {
            Collection<Object> objects = actionMethodParamList.get(actionMethodParameterType);

            Object o = objects.stream().findFirst().orElse(actionMethodParameterType.isInstance(Object.class) ? null : 0);
            realParams.add(o);
            if (o != null) {
                objects.remove(o);
            }
        }
        return realParams;
    }
}
