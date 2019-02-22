package rxweb.mapping;

import io.netty.handler.codec.http.HttpMethod;

import java.util.Objects;

/**
 * 请求条件，即method和url
 * @author zhangjessey
 */
public class Condition<T> {
    private HttpMethod httpMethod;
    private String url;

    public Condition(HttpMethod httpMethod, String url) {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition<?> condition = (Condition<?>) o;
        return httpMethod.equals(condition.httpMethod) && url.equals(condition.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, url);
    }
}
