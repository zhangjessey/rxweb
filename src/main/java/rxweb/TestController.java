package rxweb;

import rx.Observable;
import rxweb.annotation.Controller;
import rxweb.annotation.RequestBody;
import rxweb.annotation.RequestMapping;
import rxweb.bean.Params;

import java.util.HashMap;

/**
 * @author zhangjessey
 */
@Controller
public class TestController {

    @RequestMapping.Get(value = "/hi")
    public Observable<String> testMethodNoParam() {

        return Observable.just("hello");
    }


    @RequestMapping.Get(value = "/hehe")
    public Observable<String> testMethodWithParam(Params params) {

        return Observable.just("haha".concat(params.toString()));
    }

    @RequestMapping.Post(value = "/postTest/{c}")
    public Observable<String> postTest(Params params, int c) {
        return getStr(params, c);
    }

    @RequestMapping.Delete(value = "/postTest/{c}")
    public Observable<String> deleteTest(Params params, int c) {
        return getStr(params, c);
    }

    @RequestMapping.Put(value = "/postTest/{c}")
    public Observable<String> putTest(Params params, int c) {
        return getStr(params, c);
    }

    private Observable<String> getStr(Params params, int c) {
        Object a = params.getMap().get("a");
        String paramValue = (String) (a);
        String pathValue = String.valueOf(c);
        return Observable.just("echo".concat(paramValue).concat(pathValue));
    }

    @RequestMapping.Post(value = "/postReturnBean/{c}")
    public Observable<Params> postReturnBean(int c, Params params) {
        HashMap<String, Object> stringStringHashMap = new HashMap<>(3);
        stringStringHashMap.put("a", "1");
        stringStringHashMap.put("b", "2");
        stringStringHashMap.put("c", "3");
        Params p = new Params(stringStringHashMap);
        return Observable.just(p);

    }

    /**
     * curl -X POST \
     * 'http://localhost:8080/postBean/10?a=1' \
     * -H 'Content-Type: application/json' \
     * -d '{"id":1,"name":"hehe"}'
     */
    @RequestMapping.Post(value = "/postBean/{c}")
    public Observable<Params> postBean(@RequestBody User user, int c, Params params) {
        HashMap<String, Object> stringStringHashMap = new HashMap<>(3);
        stringStringHashMap.put("result", "success");
        Params p = new Params(stringStringHashMap);

        return Observable.just(p);

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
