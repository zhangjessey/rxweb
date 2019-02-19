package rxweb.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author zhangjessey
 */
public class DefaultConverter implements Converter {

    @Override
    public <T> String serialize(T t) {
        try {
            return new ObjectMapper().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public <T> T deserialize(String string, Class<T> tClass) {
        try {
            return new ObjectMapper().readValue(string, tClass);
        } catch (IOException e) {
            return null;
        }
    }
}
