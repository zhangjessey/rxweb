package rxweb.server;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.RequestHandler;
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
 * 初始化配置
 *
 * @author zhangjessey
 */
class BootstrapConfig {

    private static Set<Class<?>> CONTROLLER_CLASS_SET;
    static Map<Class<?>, Object> CONTROLLER_CLASS_OBJECT_MAP;
    static Map<Condition<HttpServerRequest>, RequestHandler<ByteBuf, ByteBuf>> CONDITION_REQUEST_HANDLER_MAP = new LinkedHashMap<>();


    static {

        final Logger logger = LoggerFactory.getLogger(BootstrapConfig.class);
        Reflections reflections = new Reflections("rxweb.*");
        CONTROLLER_CLASS_SET = reflections.getTypesAnnotatedWith(Controller.class);

        CONTROLLER_CLASS_OBJECT_MAP = CONTROLLER_CLASS_SET.stream().map(aClass -> {
            Object o;
            try {
                o = aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e.getMessage());
                return null;
            }
            return o;

        }).filter(Objects::nonNull).collect(Collectors.toMap(Object::getClass, Function.identity()));


        BootstrapConfig.CONTROLLER_CLASS_SET.forEach(aClass -> {
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                String path;
                Condition<HttpServerRequest> condition = null;

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
                    condition = new Condition<>(HttpMethod.GET, path);
                } else if (method.isAnnotationPresent(RequestMapping.Post.class)) {
                    path = method.getAnnotation(RequestMapping.Post.class).value();
                    condition = new Condition<>(HttpMethod.POST, path);
                } else if (method.isAnnotationPresent(RequestMapping.Put.class)) {
                    path = method.getAnnotation(RequestMapping.Put.class).value();
                    condition = new Condition<>(HttpMethod.PUT, path);
                } else if (method.isAnnotationPresent(RequestMapping.Delete.class)) {
                    path = method.getAnnotation(RequestMapping.Delete.class).value();
                    condition = new Condition<>(HttpMethod.DELETE, path);
                } else {
                    needPut = false;
                }

                if (needPut) {
                    CONDITION_REQUEST_HANDLER_MAP.put(condition, new Handler(aClass, method, parameterType));
                }
            }

        });
    }
}
