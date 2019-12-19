import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Start");

            String myFile1 = "{" +
                    "\"id_1\": {\"a\": {\"key1\":\"value_1\"},\"b\": {\"key1\":\"value_1\"}}," +
                    "\"id_2\": { " +
                    "\"some_key\": \"some_value\" " +
                    "}," +
                    "\"id_3\": { " +
                    "   \"a\": {\"key1\":\"value_1\"}, " +
                    "\"b\": {\"key1\":\"value_1\"} " +
                    "}}";

            String myFile2 = "{" +
                    "\"id_1\": {" +
                    " \"a\": {\"key1\":\"value_1\"}, " +
                    "\"b\": {\"key1\":\"value_1\"}" +
                    "}," +
                    "\"id_2\": {" +
                        "\"some_key\": \"some_value\" " +
                    "}," +
                    "\"id_3\": { " +
                        "\"a\": {\"key1\":\"value_2\"}, " +
                        "\"b\": {\"key1\":\"value_2\"}" +
                    "}}";

            JsonNode file1 = JsonParser.stringToJson(myFile1);
            JsonNode file2 = JsonParser.stringToJson(myFile2);

            Iterator<Map.Entry<String, JsonNode>> node1 = file1.fields();
            Iterator<Map.Entry<String, JsonNode>> node2 = file2.fields();

            List<Iterator<Map.Entry<String, JsonNode>>> listOfFiles = new ArrayList<>();
            listOfFiles.add(node1);
            listOfFiles.add(node2);

            HashMap<String, ArrayList<ObjectNode>> map = new HashMap<>();

            for (int i=0; i<listOfFiles.size(); i++) {
                map = creatingMap(listOfFiles.get(i), map);
            }

            // map Will use to create json files

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static HashMap<String, ArrayList<ObjectNode>> creatingMap (Iterator<Map.Entry<String, JsonNode>> node, HashMap<String, ArrayList<ObjectNode>> map){

        // Going over the keys in the json file
        node.forEachRemaining(jsonKeys -> {

            // If the key id not in the map yet
            if (map.get(jsonKeys.getKey()) == null) {

                ArrayList<ObjectNode> array = new ArrayList<ObjectNode>();

                // Creating a list of objects under this key
                jsonKeys.getValue().fields().forEachRemaining(value-> {
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode jObject = mapper.createObjectNode();
                    jObject.put(value.getKey(), value.getValue());
                    array.add(jObject);
                });

                map.put(jsonKeys.getKey(), array);

            } else {
                ArrayList<ObjectNode> array = new ArrayList<ObjectNode>();
                array = map.get(jsonKeys.getKey());
                ArrayList<ObjectNode> finalArray = array;

                // Detecting duplicates in the exist list
                jsonKeys.getValue().fields().forEachRemaining(value-> {
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode jObject = mapper.createObjectNode();
                    jObject.put(value.getKey(), value.getValue());

                    if (!finalArray.contains(jObject)){
                        finalArray.add(jObject);
                    }
                });

                map.put(jsonKeys.getKey(), finalArray);
            }
        });

        return map;
    }
}