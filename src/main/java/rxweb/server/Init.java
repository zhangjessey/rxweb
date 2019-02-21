package rxweb.server;

import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rxweb.annotation.Controller;
import rxweb.annotation.RequestBody;
import rxweb.annotation.RequestMapping;
import rxweb.mapping.Condition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author zhangjessey
 */
public class Init {

    public static Set<Class<?>> ControllerClasses;
    public static Map<Class<?>, Object> BeanMap;
    public static Map<Condition<HttpServerRequest>, Handler> handlers = new LinkedHashMap<>();


    static {

        final Logger logger = LoggerFactory.getLogger(Init.class);
        Reflections reflections = new Reflections("rxweb.*");
        ControllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        BeanMap = ControllerClasses.stream().map(aClass -> {
            Object o;
            try {
                o = aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e.getMessage());
                return null;
            }
            return o;

        }).filter(Objects::nonNull).collect(Collectors.toMap(Object::getClass, Function.identity()));

        // handlers.put(MappingCondition.Builder.from("/favicon.ico").method(rxweb.http.Method.GET).build(), ((request, response) -> {
        //     response.status(Status.OK).content(ByteBuffer.wrap("no favicon.ico".getBytes(StandardCharsets.UTF_8)));
        // }));

        Init.ControllerClasses.forEach(aClass -> {
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                String path = null;
                Condition condition = null;

                Class<?> parameterType = null;
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                Class<?>[] parameterTypes = method.getParameterTypes();
                int parameterCount = method.getParameterCount();
                boolean needBreak = false;
                for (int i = 0; i < parameterCount; i++) {
                    if (needBreak) {
                        break;
                    }
                    for (Annotation annotation : parameterAnnotations[i]) {
                        if (annotation.annotationType().isAssignableFrom(RequestBody.class)) {
                            System.out.println(annotation.annotationType());
                            parameterType = parameterTypes[i];
                            needBreak = true;
                            break;
                        }
                    }

                }
                boolean needPut = true;

                if (method.isAnnotationPresent(RequestMapping.Get.class)) {
                    path = method.getAnnotation(RequestMapping.Get.class).value();
                    condition = new Condition<HttpServerRequest>(HttpMethod.GET, path);
                } else if (method.isAnnotationPresent(RequestMapping.Post.class)) {
                    path = method.getAnnotation(RequestMapping.Post.class).value();
                    condition = new Condition<HttpServerRequest>(HttpMethod.POST, path);
                } else if (method.isAnnotationPresent(RequestMapping.Put.class)) {
                    path = method.getAnnotation(RequestMapping.Put.class).value();
                    condition = new Condition<HttpServerRequest>(HttpMethod.PUT, path);
                } else if (method.isAnnotationPresent(RequestMapping.Delete.class)) {
                    path = method.getAnnotation(RequestMapping.Delete.class).value();
                    condition = new Condition<HttpServerRequest>(HttpMethod.DELETE, path);
                } else {
                    needPut = false;
                }

                if (needPut) {
                    handlers.put(condition, new Handler(aClass, method, parameterType));
                }
            }

        });
    }
}
