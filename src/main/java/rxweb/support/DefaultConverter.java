package rxweb.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author zhangjessey
 */
public class DefaultConverter implements Converter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public <T> String serialize(T t) {
        try {
            return new ObjectMapper().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public <T> T deserialize(String string, Class<T> tClass) {
        try {
            return new ObjectMapper().readValue(string, tClass);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
