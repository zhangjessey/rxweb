package rxweb.bean;

import java.util.Map;
import java.util.stream.Collectors;

/**
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

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return map.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.joining(":"));
    }
}
