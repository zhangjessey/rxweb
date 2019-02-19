package rxweb.support;

/**
 * @author zhangjessey
 */
public interface Converter {

    public <T> String serialize(T t);

    public <T> T deserialize(String string, Class<T> tClass);
}
