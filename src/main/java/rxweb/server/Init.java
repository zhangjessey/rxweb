package rxweb.server;

import org.reflections.Reflections;
import rx.Observable;
import rxweb.annotation.Controller;
import rxweb.annotation.RequestMapping;
import rxweb.http.Request;
import rxweb.http.Status;
import rxweb.mapping.Condition;
import rxweb.mapping.MappingCondition;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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
    public static Map<Condition<Request>, ServerHandler> handlers = new LinkedHashMap<>();


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

        handlers.put(MappingCondition.Builder.from("/favicon.ico").method(rxweb.http.Method.GET).build(), ((request, response) -> {
            response.status(Status.OK).content(Observable.just(ByteBuffer.wrap("no favicon.ico".getBytes(StandardCharsets.UTF_8))));
        }));

        Init.ControllerClasses.forEach(aClass -> {
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.Get.class)) {
                    String path = method.getAnnotation(RequestMapping.Get.class).value();
                    handlers.put(MappingCondition.Builder.from(path).method(rxweb.http.Method.GET).build(), new Handler(aClass, method));
                }
            }

        });
    }
}
