import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonParser {
    private static ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static JsonNode stringToJson(String json){
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonNode objectToJson(Object obj){
        try {
            return stringToJson(mapper.writeValueAsString(obj));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonToObject(JsonNode json, Class<T> objectClass){
        try {
            return mapper.readValue(json.toString(), objectClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }
}
