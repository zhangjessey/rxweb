package rxweb.support;

/**
 * 转换器，负责序列化与反序列化
 * @author zhangjessey
 */
public interface Converter {

    public <T> String serialize(T t);

    public <T> T deserialize(String string, Class<T> tClass);
}
