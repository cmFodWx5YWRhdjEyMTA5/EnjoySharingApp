package enjoysharing.enjoysharing.Serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import enjoysharing.enjoysharing.DataObject.JSONObjectBase;

public class JSONObjectBaseSerializer implements JsonSerializer<JSONObjectBase> {
    @Override
    public JsonElement serialize(JSONObjectBase src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getActivityClass().toString()+","+src.getVoiceCommands()+","+src.getAction());
    }
}