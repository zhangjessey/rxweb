package rxweb.support;

/**
 * 转换器，负责序列化与反序列化
 * @author zhangjessey
 */
public interface Converter {

    /**
     * 序列化为字符串
     */
    public <T> String serialize(T t);

    /**
     * 反序列化为一个对象
     */
    public <T> T deserialize(String string, Class<T> tClass);
}
