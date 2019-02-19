package rxweb;

import rx.Observable;
import rxweb.annotation.Controller;
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

    @RequestMapping.Post(value = "/postTestBean/{c}")
    public Observable<Params> postTestBean(int c, Params params) {
        HashMap<String, Object> stringStringHashMap = new HashMap<>(3);
        stringStringHashMap.put("a", "1");
        stringStringHashMap.put("b", "2");
        stringStringHashMap.put("c", "3");

        Params p = new Params(stringStringHashMap);

        return Observable.just(p);

    }
}
