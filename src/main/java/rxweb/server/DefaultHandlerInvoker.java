package rxweb.server;

import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import rxweb.bean.Params;
import rxweb.support.WebUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author huangyong
 * @author zhangjessey
 */
public class DefaultHandlerInvoker implements HandlerInvoker {


    @Override
    public Object invokeHandler(HttpServerRequest request, Handler handler) {
        try {
            // 获取 Action 相关信息
            Class<?> actionClass = handler.getActionClass();
            Method actionMethod = handler.getActionMethod();
            // 从 BeanHelper 中创建 Action 实例
            Object actionInstance = Init.BeanMap.get(actionClass);
            // 创建 Action 方法的参数列表
            List<Object> actionMethodParamList = createActionMethodParamList(request, handler);
            // 检查参数列表是否合法
            checkParamList(actionMethod, actionMethodParamList);
            // 调用 Action 方法
            Object actionMethodResult = invokeActionMethod(actionMethod, actionInstance, actionMethodParamList);
            return actionMethodResult;
        } catch (Exception e) {
            return null;
        }

    }

    public List<Object> createActionMethodParamList(HttpServerRequest request, Handler handler) throws Exception {
        // 定义参数列表
        List<Object> paramList = new ArrayList<>();
        // 获取 Action 方法参数类型
        Class<?>[] actionParamTypes = handler.getActionMethod().getParameterTypes();
        // 添加路径参数列表（请求路径中的带占位符参数）
        if (handler.getRequestPathMatcher() != null) {
            paramList.addAll(createPathParamList(handler.getRequestPathMatcher(), actionParamTypes));
        }
        // 分两种情况进行处理
        // if (UploadHelper.isMultipart(request)) {
        //     // 添加 Multipart 请求参数列表
        //     paramList.addAll(UploadHelper.createMultipartParamList(request));
        // } else {
        // 添加普通请求参数列表（包括 Query String 与 Form Data）
        Map<String, Object> requestParamMap = WebUtils.getRequestParamMap(request);
        if (requestParamMap != null && requestParamMap.size() != 0) {
            paramList.add(new Params(requestParamMap));
            // }
        }
        // 返回参数列表
        return paramList;
    }

    private List<Object> createPathParamList(Matcher requestPathMatcher, Class<?>[] actionParamTypes) {
        // 定义参数列表
        List<Object> paramList = new ArrayList<Object>();
        // 遍历正则表达式中所匹配的组
        for (int i = 1; i <= requestPathMatcher.groupCount(); i++) {
            // 获取请求参数
            String param = requestPathMatcher.group(i);
            // 获取参数类型（支持四种类型：int/Integer、long/Long、double/Double、String）
            Class<?> paramType = actionParamTypes[i - 1];
            if (paramType.equals(int.class) || paramType.equals(Integer.class)) {
                paramList.add(Integer.valueOf(param));
            } else if (paramType.equals(long.class) || paramType.equals(Long.class)) {
                paramList.add(Long.valueOf(param));
            } else if (paramType.equals(double.class) || paramType.equals(Double.class)) {
                paramList.add(Double.valueOf(param));
            } else if (paramType.equals(String.class)) {
                paramList.add(param);
            }
        }
        // 返回参数列表
        return paramList;
    }

    private Object invokeActionMethod(Method actionMethod, Object actionInstance, List<Object> actionMethodParamList) throws IllegalAccessException, InvocationTargetException {
        // 通过反射调用 Action 方法
        actionMethod.setAccessible(true); // 取消类型安全检测（可提高反射性能）
        return actionMethod.invoke(actionInstance, actionMethodParamList.toArray());
    }

    private void checkParamList(Method actionMethod, List<Object> actionMethodParamList) {
        // 判断 Action 方法参数的个数是否匹配
        Class<?>[] actionMethodParameterTypes = actionMethod.getParameterTypes();
        if (actionMethodParameterTypes.length != actionMethodParamList.size()) {
            String message = String.format("因为参数个数不匹配，所以无法调用 Action 方法！原始参数个数：%d，实际参数个数：%d", actionMethodParameterTypes.length, actionMethodParamList.size());
            throw new RuntimeException(message);
        }
    }
}
