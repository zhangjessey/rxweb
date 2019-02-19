package rxweb.mapping;

import io.netty.handler.codec.http.HttpMethod;

/**
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
}
