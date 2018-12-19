package enjoysharing.enjoysharing.Business;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CardHome;
import enjoysharing.enjoysharing.R;

public class BusinessCards extends BusinessBase {

    // Simple contructor
    public BusinessCards(Activity activity) {
        super(activity);
    }
    // Used to draw home card on home table
    @Override
    public void DrawHomeCardsOnTable(CardCollection homeCards)
    {
        TableLayout homeTable = (TableLayout) activity.findViewById(R.id.tblHomeCards);
        DrawCardsOnTable(homeCards, homeTable);
    }
    // Used to draw search card on search table
    @Override
    public void DrawSearchCardsOnTable(CardCollection searchCards)
    {
        TableLayout searchTable = (TableLayout) activity.findViewById(R.id.tblSearchCards);
        DrawCardsOnTable(searchCards, searchTable);
    }
    // Used to draw cards on table input
    protected void DrawCardsOnTable(CardCollection cards, TableLayout table)
    {
        table.removeAllViews();
        for (int i=0; i<cards.List().size(); i++) {
            CardHome card = (CardHome)cards.List().get(i);
            TableRow row = (TableRow) LayoutInflater.from(activity).inflate(R.layout.card_home, null);
            RelativeLayout relLayout = (RelativeLayout)row.getChildAt(0);
            // row.getChildAt(0) è il relative layout che contiene tutti gli elementi
            for (int x = 0; x < relLayout.getChildCount(); x++)
            {
                View child = relLayout.getChildAt(x);
                if(child.getId() == R.id.txtUserCardHome)
                    ((TextView)child).setText(card.getUsername());
                if(child.getId() == R.id.txtTitleCardHome)
                    ((TextView)child).setText(card.getTitle());
                if(child.getId() == R.id.txtContentCardHome)
                    ((TextView)child).setText(card.getContent());
            }
            table.addView(row);
        }
    }
}
