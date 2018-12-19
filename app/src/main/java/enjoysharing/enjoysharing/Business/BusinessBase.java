package enjoysharing.enjoysharing.Business;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CurrentUser;

public class BusinessBase {

    protected Activity activity;

    public BusinessBase(Activity activity)
    {
        this.activity = activity;
    }
    // BusinessCards method
    public void DrawHomeCardsOnTable(CardCollection homeCards){}
    // BusinessCards method
    public void DrawSearchCardsOnTable(CardCollection homeCards){}

}
