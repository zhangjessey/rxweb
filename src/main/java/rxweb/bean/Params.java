package rxweb.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * url参数
 *
 * @author zhangjessey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Params {
    private Map<String, Object> map;

    @Override
    public String toString() {
        return String.join(":", map.keySet());
    }
}

