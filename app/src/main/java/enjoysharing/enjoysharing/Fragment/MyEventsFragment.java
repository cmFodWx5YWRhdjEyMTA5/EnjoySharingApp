package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.support.v7.widget.TooltipCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import enjoysharing.enjoysharing.Activity.IUEventActivity;
import enjoysharing.enjoysharing.Activity.RequestListActivity;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CardMyEvent;
import enjoysharing.enjoysharing.R;

public class MyEventsFragment extends FragmentBase {

    protected TableLayout tableCardsMyEvent;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_my_events, container, false);
        business = new BusinessBase(activity);
        tableCardsMyEvent = (TableLayout) vMain.findViewById(R.id.tableCardsMyEvent);
        setFormView((FrameLayout) vMain.findViewById(R.id.main_frame_my_event));
        super.onCreateView(inflater,container,savedInstanceState);
        return vMain;
    }
    @Override
    protected void ShowProgress(boolean state)
    {
        activity.showProgress(state, formView, progressView);
    }
    @Override
    public void StartFragment()
    {
        LoadMyEvents();
    }

    protected CardCollection myEventCards;
    // TODO
    // Load with server call
    protected void LoadMyEvents()
    {

        if (mTask != null) {
            myEventCards = new CardCollection();
            DrawCardsOnTable(myEventCards,tableCardsMyEvent);
            return;
        }
        ShowProgress(true);
        mTask = new FragmentRequestTask();
        mTask.execute((Void) null);
    }
    // TODO
    // Server call
    @Override
    protected void DoInBackground()
    {
        myEventCards = business.GetMyEventsCards();
    }

    @Override
    protected void OnRequestPostExecute()
    {
        // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
        DrawCardsOnTable(myEventCards,tableCardsMyEvent);
    }

    // Used by requests tabs
    protected void DrawCardsOnTable(CardCollection cards, TableLayout table)
    {
        table.removeAllViews();
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(85);
        int parentTollerancePX = 5;
        for (int i=0; i<cards.List().size(); i++) {
            final CardMyEvent card = (CardMyEvent)cards.List().get(i);
            TableRow row = (TableRow) LayoutInflater.from(activity).inflate(R.layout.card_my_event, null);
            LinearLayout relLayout = (LinearLayout)row.getChildAt(0);
            // row.getChildAt(0) è il relative layout che contiene tutti gli elementi
            TextView txtUserCardMyEvent = (TextView)relLayout.findViewById(R.id.txtUserCardMyEvent);
            // Set width based on screen percentage
            txtUserCardMyEvent.setWidth(txtUserTitleWidth);
            txtUserCardMyEvent.setText(card.getUsername());
            TextView txtTitleCardMyEvent = (TextView)relLayout.findViewById(R.id.txtTitleCardMyEvent);
            // Set width based on screen percentage
            txtTitleCardMyEvent.setWidth(txtUserTitleWidth);
            txtTitleCardMyEvent.setText(card.getTitle());
            TextView txtContentCardMyEvent = (TextView)relLayout.findViewById(R.id.txtContentCardMyEvent);
            // Set the same width of parent - tollerance
            txtContentCardMyEvent.setWidth(((LinearLayout)txtContentCardMyEvent.getParent()).getWidth()-parentTollerancePX);
            txtContentCardMyEvent.setText(card.getContent());
            TextView txtNumberPerson = (TextView)relLayout.findViewById(R.id.txtNumberPerson);
            txtNumberPerson.setText(card.getRequestNumber() + "/" + card.getMaxRequest());
            txtNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(activity.getBaseContext(),RequestListActivity.class, card, true);
                }
            });
            ImageView imgBtnGender = (ImageView)relLayout.findViewById(R.id.imgBtnGender);
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderIndex()));
            TooltipCompat.setTooltipText(imgBtnGender, business.GetGenderItem(card.getGenderIndex()));
            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v, card.getIdCard());
                }
            });
            table.addView(row);
        }
    }

    @Override
    protected void onRowClick(View v, int rowId)
    {
        CardMyEvent card = (CardMyEvent) myEventCards.GetCard(rowId);
        if(card != null)
        {
            SwipeDownOpenActivity(activity.getBaseContext(), IUEventActivity.class, card);
        }
    }

}
