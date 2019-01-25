package enjoysharing.enjoysharing.Business;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import enjoysharing.enjoysharing.Activity.BaseActivity;
import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CardHome;
import enjoysharing.enjoysharing.DataObject.CardMyEvent;
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
    @Override
    public CardCollection GetHomeCards(String JSONStr)
    {
        Gson gson = new Gson();
        Type founderListType = new TypeToken<ArrayList<CardHome>>(){}.getType();
        ArrayList<CardHome> cardList = gson.fromJson(JSONStr, founderListType);
        CardCollection cards = new CardCollection();
        for (CardHome card : cardList)
        {
            cards.Add(card);
        }
        return cards;
    }
    @Override
    public CardCollection GetMyEventsCards(String JSONStr)
    {
        Gson gson = new Gson();
        Type founderListType = new TypeToken<ArrayList<CardMyEvent>>(){}.getType();
        ArrayList<CardMyEvent> cardList = gson.fromJson(JSONStr, founderListType);
        CardCollection cards = new CardCollection();
        for (CardMyEvent card : cardList)
        {
            cards.Add(card);
        }
        return cards;
    }
}
