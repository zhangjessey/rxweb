package rxweb.server;

import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import org.reflections.Reflections;
import rxweb.annotation.Controller;
import rxweb.annotation.RequestMapping;
import rxweb.mapping.Condition;

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
        Reflections reflections = new Reflections("rxweb.*");
        ControllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        BeanMap = ControllerClasses.stream().map(aClass -> {
            Object o;
            try {
                o = aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
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
                if (method.isAnnotationPresent(RequestMapping.Get.class)) {
                    String path = method.getAnnotation(RequestMapping.Get.class).value();
                    handlers.put(new Condition<HttpServerRequest>(HttpMethod.GET, path), new Handler(aClass, method));
                } else if (method.isAnnotationPresent(RequestMapping.Post.class)) {
                    String path = method.getAnnotation(RequestMapping.Post.class).value();
                    handlers.put(new Condition<HttpServerRequest>(HttpMethod.POST, path), new Handler(aClass, method));
                }
            }

        });
    }
}
