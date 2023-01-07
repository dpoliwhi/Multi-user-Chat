package edu.school21.sockets.json;

import edu.school21.sockets.models.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class JSONConverter {
    public static JSONMessage parseToObject(String message) {
        JSONMessage newMessage = new JSONMessage();
        JSONParser parser = new JSONParser();
        try {
            JSONObject messageObject = (JSONObject) parser.parse(message);
            newMessage.setMessage((String) messageObject.get("message"));
            String time = (String) messageObject.get("time");
            newMessage.setTime(LocalDateTime.parse(time));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return newMessage;
    }

    public static JSONObject makeJSONObject(String message, LocalDateTime time) {
        try {
            Map<String, String> mapMessage = new HashMap<>();
            mapMessage.put("message", message);
            mapMessage.put("time", time.toString());
            return new JSONObject(mapMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
