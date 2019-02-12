package rxweb.server;

import org.reflections.Reflections;
import rxweb.annotation.Controller;
import rxweb.annotation.RequestMapping;
import rxweb.http.Request;
import rxweb.mapping.Condition;
import rxweb.mapping.MappingCondition;

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
