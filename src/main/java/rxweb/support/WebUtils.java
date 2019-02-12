package rxweb.support;


import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rxweb.server.ServerRequest;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangyong
 * @author zhangjessey
 * 从请求中获取所有参数（当参数名重复时，用后者覆盖前者）
 */
public class WebUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

    /**
     * 从请求中获取所有参数（当参数名重复时，用后者覆盖前者）
     */
    public static Map<String, Object> getRequestParamMap(ServerRequest request) {
        Map<String, Object> paramMap = new LinkedHashMap<String, Object>();
        try {
            HttpMethod method = new HttpMethod(request.getMethod().getName());
            if ((HttpMethod.GET.equals(method))) {
                QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri(), Charset.forName("UTF-8"));
                Map<String, List<String>> uriAttributes = queryDecoder.parameters();
                //此处仅打印请求参数（你可以根据业务需求自定义处理）
                for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
                    for (String attrVal : attr.getValue()) {
                        //logger.info(attr.getKey() + "=" + attrVal);
                        paramMap.put(attr.getKey(), attr.getValue());
                    }
                }

            }
            // if (method.equalsIgnoreCase("put") || method.equalsIgnoreCase("delete")) {
            //     String queryString = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
            //     if (StringUtil.isNotEmpty(queryString)) {
            //         String[] qsArray = StringUtil.splitString(queryString, "&");
            //         if (ArrayUtil.isNotEmpty(qsArray)) {
            //             for (String qs : qsArray) {
            //                 String[] array = StringUtil.splitString(qs, "=");
            //                 if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
            //                     String paramName = array[0];
            //                     String paramValue = array[1];
            //                     if (checkParamName(paramName)) {
            //                         if (paramMap.containsKey(paramName)) {
            //                             paramValue = paramMap.get(paramName) + StringUtil.SEPARATOR + paramValue;
            //                         }
            //                         paramMap.put(paramName, paramValue);
            //                     }
            //                 }
            //             }
            //         }
            //     }
            // } else {
            //     Enumeration<String> paramNames = request.getParameterNames();
            //     while (paramNames.hasMoreElements()) {
            //         String paramName = paramNames.nextElement();
            //         if (checkParamName(paramName)) {
            //             String[] paramValues = request.getParameterValues(paramName);
            //             if (ArrayUtil.isNotEmpty(paramValues)) {
            //                 if (paramValues.length == 1) {
            //                     paramMap.put(paramName, paramValues[0]);
            //                 } else {
            //                     StringBuilder paramValue = new StringBuilder("");
            //                     for (int i = 0; i < paramValues.length; i++) {
            //                         paramValue.append(paramValues[i]);
            //                         if (i != paramValues.length - 1) {
            //                             paramValue.append(StringUtil.SEPARATOR);
            //                         }
            //                     }
            //                     paramMap.put(paramName, paramValue.toString());
            //                 }
            //             }
            //         }
            //     }
            //}
        } catch (Exception e) {
            logger.error("获取请求参数出错！", e);
            throw new RuntimeException(e);
        }
        return paramMap;
    }
}