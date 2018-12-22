package enjoysharing.enjoysharing.Business;

import android.app.Activity;
import android.content.res.Configuration;

import enjoysharing.enjoysharing.DataObject.CardCollection;

public class BusinessBase {

    protected Activity activity;

    public BusinessBase(Activity activity)
    {
        this.activity = activity;
    }
    // Used to convert width based on percentage
    protected int ConvertWidthBasedOnPerc(int perc)
    {
        Configuration configuration = activity.getResources().getConfiguration();
        int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        return (screenWidth/100)*perc;
    }
    // BusinessCards method
    public void DrawHomeCardsOnTable(CardCollection homeCards){}
    // BusinessCards method
    public void DrawSearchCardsOnTable(CardCollection homeCards){}

}
