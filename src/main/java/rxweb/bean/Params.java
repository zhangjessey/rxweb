package rxweb.bean;

import java.util.Map;

/**
 * url参数
 *
 * @author zhangjessey
 */
public class Params {
    private Map<String, Object> map;

    public Params(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    @Override
    public String toString() {
        return String.join(":", map.keySet());
    }
}
