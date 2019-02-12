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
}
