package enjoysharing.enjoysharing.Business;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TableLayout;

import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.R;

public class BusinessBase {

    protected Activity activity;

    public BusinessBase(Activity activity)
    {
        this.activity = activity;
    }
    // Used to convert width based on percentage
    public int ConvertWidthBasedOnPerc(int perc)
    {
        Configuration configuration = activity.getResources().getConfiguration();
        int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        return (screenWidth/100)*perc;
    }
    // Used to manage button request partecipate
    public void SetButtonRequest(Button btn, boolean state)
    {
        if(state)
        {
            btn.setText(R.string.txtRequestPartecipate);
            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_request_custom,0,0,0);
        }
        else
        {
            btn.setText(R.string.txtRequestPartecipateReverse);
            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_request_partecipate_reverse,0,0,0);
        }
        btn.setHint(state?"1":"0");
    }
}
