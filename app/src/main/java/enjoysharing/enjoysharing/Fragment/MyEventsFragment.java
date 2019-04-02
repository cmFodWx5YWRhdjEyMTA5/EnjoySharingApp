package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import enjoysharing.enjoysharing.Activity.IUEventActivity;
import enjoysharing.enjoysharing.Activity.RequestListActivity;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Business.BusinessJSON;
import enjoysharing.enjoysharing.DataObject.Card.CardCollection;
import enjoysharing.enjoysharing.DataObject.Card.CardMyEvent;
import enjoysharing.enjoysharing.R;

public class MyEventsFragment extends FragmentBase {

    protected TableLayout tableCardsMyEvent;
    protected ScrollView tableMyEventScrollView;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_my_events, container, false);
        tableCardsMyEvent = (TableLayout) vMain.findViewById(R.id.tableCardsMyEvent);
        final FrameLayout main_frame_my_event = (FrameLayout) vMain.findViewById(R.id.main_frame_my_event);
        setFormView(main_frame_my_event);
        tableMyEventScrollView = (ScrollView)vMain.findViewById(R.id.tableMyEventScrollView);
        setTableReloadScrollView(tableMyEventScrollView);
        reloadOnSwipeBottom = true;
        super.onCreateView(inflater,container,savedInstanceState);
        return vMain;
    }
    @Override
    protected boolean CheckForCurrentFragment() { return parentFragment.CheckForCurrentFragment() && parentFragment.getCurrentMenuPosition()==2; }
    @Override
    protected void ShowProgress(boolean state)
    {
        ShowProgressPassView(state);
    }
    @Override
    public void StartFragment()
    {
        business = new BusinessBase(activity);
        if(tableCardsMyEvent != null)
        {
            super.StartFragment();
            LoadTable();
        }
    }

    protected CardCollection myEventCards;

    // Load with server call
    @Override
    protected void LoadTable()
    {

        if (mTask != null) {
            myEventCards = new CardCollection();
            DrawCardsOnTable(myEventCards,tableCardsMyEvent);
            return;
        }
        //ShowProgress(true);
        mTask = new FragmentRequestTask(false, true, "EventServlet");
        mTask.AddParameter("RequestType","M");
        try
        {
            mTask.execute();
        }
        catch (Exception e)
        {
            activity.retObj.setStateResponse(false);
            activity.retObj.setMessage("GeneralError");
        }
    }
    // Server call
    @Override
    protected void DoInBackground()
    {
        if(activity.simulateCall)
            myEventCards = business.GetMyEventsCards(null);
        else
            myEventCards = new BusinessJSON(activity).GetMyEventsCards(activity.retObj.getMessage());
    }

    @Override
    protected void OnRequestPostExecute()
    {
        super.OnRequestPostExecute();
        if(requestSuccess && activity.retObj.isOkResponse())
        {
            if(myEventCards != null)
                // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
                DrawCardsOnTable(myEventCards,tableCardsMyEvent);
        }
        else
            ShowShortMessage(activity.retObj.getMessage());
    }

    // Used by requests tabs
    protected void DrawCardsOnTable(CardCollection cards, TableLayout table)
    {
        super.DrawCardsOnTable(cards,table);
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(85);
        int parentTollerancePX = 5;
        for (int i=0; i<cards.List().size(); i++) {
            final CardMyEvent card = (CardMyEvent)cards.List().get(i);
            if(CardAlreadyExists(card)) continue;
            TableRow row = (TableRow) LayoutInflater.from(activity).inflate(R.layout.card_my_event, null);
            
            LinearLayout relLayout = (LinearLayout)row.getChildAt(0);
            
            // row.getChildAt(0) è il relative layout che contiene tutti gli elementi
            TextView txtUserCard = (TextView)relLayout.findViewById(R.id.txtUserCard);
            // Set width based on screen percentage
            txtUserCard.setWidth(txtUserTitleWidth);
            txtUserCard.setText(card.getUserName());
            
            TextView txtTitleCard = (TextView)relLayout.findViewById(R.id.txtTitleCard);
            // Set width based on screen percentage
            txtTitleCard.setWidth(txtUserTitleWidth);
            txtTitleCard.setText(card.getTitle());
            
            TextView txtContentCard = (TextView)relLayout.findViewById(R.id.txtContentCard);
            // Set the same width of parent - tollerance
            txtContentCard.setWidth(((LinearLayout)txtContentCard.getParent()).getWidth()-parentTollerancePX);
            txtContentCard.setText(card.getContent());

            ImageView imgUserCard = (ImageView)relLayout.findViewById(R.id.imgUserCard);
            imgUserCard.setClipToOutline(true);
            business.LoadUserImage(imgUserCard);

            TextView txtDateEvent = (TextView)relLayout.findViewById(R.id.txtDateEvent);
            txtDateEvent.setText(business.GetDateString(card.getDateEvent()));
            
            TextView txtNumberPerson = (TextView)relLayout.findViewById(R.id.txtNumberPerson);
            txtNumberPerson.setText(card.getAcceptedRequest() + "/" + card.getMaxRequest());
            
            final ImageButton imgBtnNumberPerson = (ImageButton) relLayout.findViewById(R.id.imgBtnNumberPerson);
            imgBtnNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(activity.getBaseContext(),RequestListActivity.class, card, true);
                }
            });
            
            LinearLayout layoutNumberPerson = (LinearLayout) relLayout.findViewById(R.id.layoutNumberPerson);
            layoutNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imgBtnNumberPerson.performClick();
                }
            });
            
            ImageView imgBtnGender = (ImageView)relLayout.findViewById(R.id.imgBtnGender);
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderEventId()));
            
            TextView txtGender = (TextView)relLayout.findViewById(R.id.txtGender);
            txtGender.setText(business.GetGenderItem(card.getGenderEventId()-1));
            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v, card.getEventId());
                }
            });
            
            table.addView(row);
            AddToExistingCards(card);
        }
        AddProgressToTable(table);
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
