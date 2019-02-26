package rxweb.sample;

import rx.Observable;
import rxweb.annotation.Controller;
import rxweb.annotation.RequestBody;
import rxweb.annotation.RequestMapping;
import rxweb.bean.Params;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 测试用Controller
 *
 * @author zhangjessey
 */
@Controller
public class TestController {

    @RequestMapping.Get(value = "/hi")
    public Observable<String> noParam() {

        return Observable.just("hello world");
    }


    @RequestMapping.Get(value = "/withQueryParam")
    public Observable<String> withQueryParam(Params params) {

        return Observable.just("query name:".concat(params.toString()));
    }

    @RequestMapping.Post(value = "/post/{c}")
    public Observable<String> post(Params params, int c) {
        return getStr(params, c);
    }

    @RequestMapping.Delete(value = "/delete/{c}")
    public Observable<String> delete(Params params, int c) {
        return getStr(params, c);
    }

    @RequestMapping.Put(value = "/put/{c}")
    public Observable<String> put(Params params, int c) {
        return getStr(params, c);
    }


    @RequestMapping.Post(value = "/postAndReturnBean/{c}")
    public Observable<Params> postAndReturnBean(int c, Params params) {

        HashMap<String, Object> stringStringHashMap = new HashMap<>(3);
        stringStringHashMap.put("a", "1");
        stringStringHashMap.put("b", "2");
        stringStringHashMap.put("c", "3");
        Params p = new Params(stringStringHashMap);
        return Observable.just(p);

    }


    @RequestMapping.Post(value = "/postBean/{c}")
    public Observable<Params> postBean(@RequestBody User user, int c, Params params) {

        HashMap<String, Object> stringStringHashMap = new LinkedHashMap<>(5);
        stringStringHashMap.put("result", "success");
        stringStringHashMap.put("id", user.getId());
        stringStringHashMap.put("name", user.getName());
        stringStringHashMap.put("pathValue", c);
        stringStringHashMap.put("param_a_value", params.getMap().get("a"));
        Params p = new Params(stringStringHashMap);

        return Observable.just(p);

    }


    private Observable<String> getStr(Params params, int c) {

        String a = String.join(":", params.getMap().keySet());
        String pathValue = String.valueOf(c);
        return Observable.just("query name:".concat(a).concat(",pathValue:").concat(pathValue));
    }

    public static class User {
        private String id;
        private String name;

        public User() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
