package enjoysharing.enjoysharing.Business;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import enjoysharing.enjoysharing.Activity.BaseActivity;
import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CardHome;
import enjoysharing.enjoysharing.R;

public class BusinessCards extends BusinessBase {

    // Used for rows
    protected BaseActivity activity;
    // Simple contructor
    public BusinessCards(BaseActivity activity) {
        super(activity);
        this.activity = activity;
    }
    // Used to draw home card on home table
    @Override
    public void DrawHomeCardsOnTable(CardCollection homeCards)
    {
        TableLayout homeTable = (TableLayout) activity.findViewById(R.id.tblHomeCards);
        DrawCardsOnTable(homeCards,homeTable);
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
            TextView txtUserCardHome = (TextView)relLayout.findViewById(R.id.txtUserCardHome);
            txtUserCardHome.setText(card.getUsername());
            TextView txtTitleCardHome = (TextView)relLayout.findViewById(R.id.txtTitleCardHome);
            txtTitleCardHome.setText(card.getTitle());
            TextView txtContentCardHome = (TextView)relLayout.findViewById(R.id.txtContentCardHome);
            txtContentCardHome.setText(card.getContent());
            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    activity.onRowClick(v);
                }
            });
            table.addView(row);
        }
    }
}
