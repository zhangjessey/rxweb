package rxweb.mapping;

import io.netty.handler.codec.http.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请求条件，即method和url
 *
 * @author zhangjessey
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Condition<T> {
    private HttpMethod httpMethod;
    private String url;

}
