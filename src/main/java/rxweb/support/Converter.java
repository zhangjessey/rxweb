package rxweb.support;

/**
 * 转换器，负责序列化与反序列化
 *
 * @author zhangjessey
 */
public interface Converter {

    /**
     * 序列化为字符串
     * @param t 序列化目标对象
     * @param <T> 序列化目标对象类型
     * @return 序列化后的String
     */
    <T> String serialize(T t);

    /**
     * 反序列化为一个对象
     * @param string 反序列化目标字符串
     * @param tClass 序列化后的class
     * @param <T> 序列化后的类型
     * @return tClass对应的对象
     */
    <T> T deserialize(String string, Class<T> tClass);
}
