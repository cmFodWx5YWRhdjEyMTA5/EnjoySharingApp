package enjoysharing.enjoysharing.Business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import enjoysharing.enjoysharing.Activity.BaseActivity;
import enjoysharing.enjoysharing.DataObject.ParameterCollection;

public class BusinessJSON extends BusinessBase {
    public BusinessJSON(BaseActivity activity) {
        super(activity);
    }

    @Override
    public ParameterCollection GetUserInfo(String JsonStr)
    {
        try
        {
            ParameterCollection params = new ParameterCollection();
            JSONArray jsonArray = new JSONArray(JsonStr);
            JSONObject rowJson = jsonArray.getJSONObject(0);
            Iterator<String> iter = rowJson.keys();
            String key, value;
            while (iter.hasNext()) {
                key = iter.next();
                value = rowJson.get(key).toString();
                params.Add(key,value);
            }
            return params;
        } catch (JSONException e)
        {
            return null;
        }
    }
}
