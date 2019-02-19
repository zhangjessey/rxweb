package rxweb;

import rxweb.annotation.Controller;
import rxweb.annotation.RequestMapping;
import rxweb.bean.Params;

/**
 * @author zhangjessey
 */
@Controller
public class TestController {

    @RequestMapping.Get(value = "/hi")
    public String testMethodNoParam() {

        return "hello";
    }


    @RequestMapping.Get(value = "/hehe")
    public String testMethodWithParam(Params params) {

        return "haha".concat(params.toString());
    }

    @RequestMapping.Post(value = "/postTest/{c}")
    public String postTest(Params params, int c) {
        return getStr(params, c);
    }

    @RequestMapping.Delete(value = "/postTest/{c}")
    public String deleteTest(Params params, int c) {
        return getStr(params, c);
    }

    @RequestMapping.Put(value = "/postTest/{c}")
    public String putTest(Params params, int c) {
        return getStr(params, c);
    }

    private String getStr(Params params, int c) {
        Object a = params.getMap().get("a");
        String paramValue = (String) (a);
        String pathValue = String.valueOf(c);
        return "echo".concat(paramValue).concat(pathValue);
    }
}
