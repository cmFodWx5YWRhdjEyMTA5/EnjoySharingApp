package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import enjoysharing.enjoysharing.Activity.CardDetailActivity;
import enjoysharing.enjoysharing.Activity.RequestListActivity;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Business.BusinessJSON;
import enjoysharing.enjoysharing.DataObject.Card.CardCollection;
import enjoysharing.enjoysharing.DataObject.Card.CardHome;
import enjoysharing.enjoysharing.R;

public class HomeFragment extends FragmentBase {

    protected TableLayout tableHomeCards;
    protected TableRow row_progress;
    protected ProgressBar home_progress_bar;
    protected Button btn;
    protected int Index;
    protected boolean stateRequest;
    protected int EventId;
    protected CardCollection existingCards;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_home, container, false);
        tableHomeCards = (TableLayout) vMain.findViewById(R.id.tableHomeCards);
        row_progress = (TableRow) LayoutInflater.from(activity).inflate(R.layout.progress_bar, null);
        home_progress_bar = (ProgressBar) row_progress.findViewById(R.id.progress_bar);
        final ScrollView tableHomeScrollView = (ScrollView) vMain.findViewById(R.id.tableHomeScrollView);
        tableHomeScrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (!tableHomeScrollView.canScrollVertically(1)) {
                            home_progress_bar.setVisibility(View.VISIBLE);
                            FillHomeCards();
                        }
                        if (!tableHomeScrollView.canScrollVertically(-1)) {
                        }
                    }
                });
        setFormView((FrameLayout) vMain.findViewById(R.id.main_frame_home));
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
        business = new BusinessBase(activity);
        Index = 0;
        existingCards = new CardCollection();
        FillHomeCards();
    }

    protected CardCollection homeCards;
    // User to load home cards
    // Fill cards by server call
    protected void FillHomeCards()
    {
        if (mTask != null) {
            return;
        }
        //ShowProgress(true);
        PostCall = false;
        mTask = new FragmentRequestTask(false, true, "EventServlet",(Index == 0));
        mTask.AddParameter("RequestType","H");
        mTask.AddParameter("Index",Index);
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
    // Used to send or reverse request partecipate
    protected void SendRequestPartecipate(Button btn, int EventId)
    {
        if (mTask != null) {
            business.LoadingRequestButton(btn,false);
            return;
        }
        //ShowProgress(true);
        PostCall = true;
        this.btn = btn;
        stateRequest = (this.btn.getHint() == "1");
        this.EventId = EventId;
        mTask = new FragmentRequestTask(true, false, "RequestServlet", false);
        mTask.AddParameter("RequestType",stateRequest?"NR":"DR");  // New Request or Delete Request
        mTask.AddParameter("EventId",EventId);
        mTask.AddParameter("UserId",user.getUserId());
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
        if(!PostCall)
        {
            if (activity.simulateCall)
                homeCards = business.GetHomeCards(null);
            else
                homeCards = new BusinessJSON(activity).GetHomeCards(activity.retObj.getMessage());
        }
    }

    @Override
    protected void OnRequestPostExecute()
    {
        home_progress_bar.setVisibility(View.GONE);
        if(requestSuccess && activity.retObj.isOkResponse())
        {
            if(PostCall)
            {
                business.SetButtonRequest(btn,!stateRequest);
                business.LoadingRequestButton(btn,false);
                UpdateCard();
            }
            else
            {
                if (homeCards != null)
                    // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
                    DrawCardsOnTable(homeCards, tableHomeCards);
            }
        }
        else
        {
            Toast.makeText(activity, activity.retObj.getMessage(), Toast.LENGTH_SHORT).show();
            if(PostCall)
                business.LoadingRequestButton(btn,false);
        }
    }

    protected void UpdateCard()
    {
        CardHome card = (CardHome) homeCards.GetCard(EventId);
        if(card != null)
            card.setRequestSubmitted(stateRequest);
        EventId = 0;
    }

    protected void DrawCardsOnTable(CardCollection cards, TableLayout table)
    {
        if(Index == 0)
        {
            table.removeAllViews();
        }
        else
        {
            table.removeView(row_progress);
        }
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(84);
        int parentTollerancePX = 5;
        for (int i=0; i<cards.List().size(); i++) {
            final CardHome card = (CardHome)cards.List().get(i);
            Index++;
            if(CardAlreadyExists(card)) continue;
            TableRow row = (TableRow) LayoutInflater.from(activity).inflate(R.layout.card_home, null);
            LinearLayout relLayout = (LinearLayout)row.getChildAt(0);
            // row.getChildAt(0) è il relative layout che contiene tutti gli elementi
            TextView txtUserCardHome = (TextView)relLayout.findViewById(R.id.txtUserCardHome);
            // Set width based on screen percentage
            txtUserCardHome.setWidth(txtUserTitleWidth);
            txtUserCardHome.setText(card.getUserName());
            TextView txtTitleCardHome = (TextView)relLayout.findViewById(R.id.txtTitleCardHome);
            // Set width based on screen percentage
            txtTitleCardHome.setWidth(txtUserTitleWidth);
            txtTitleCardHome.setText(card.getTitle());
            TextView txtContentCardHome = (TextView)relLayout.findViewById(R.id.txtContentCardHome);
            // Set the same width of parent - tollerance
            txtContentCardHome.setWidth(((LinearLayout)txtContentCardHome.getParent()).getWidth()-parentTollerancePX);
            txtContentCardHome.setText(card.getContent());
            TextView txtDateEvent = (TextView)relLayout.findViewById(R.id.txtDateEvent);
            txtDateEvent.setText(business.GetDateString(card.getDateEvent()));
            final ImageButton imgBtnNumberPerson = (ImageButton) relLayout.findViewById(R.id.imgBtnNumberPerson);
            imgBtnNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(activity.getBaseContext(),RequestListActivity.class, card, false);
                }
            });
            LinearLayout layoutNumberPerson = (LinearLayout) relLayout.findViewById(R.id.layoutNumberPerson);
            layoutNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imgBtnNumberPerson.performClick();
                }
            });
            final TextView txtNumberPerson = (TextView)relLayout.findViewById(R.id.txtNumberPerson);
            txtNumberPerson.setText(card.getAcceptedRequest() + "/" + card.getMaxRequest());
            ImageView imgBtnGender = (ImageView)relLayout.findViewById(R.id.imgBtnGender);
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderEventId()));
            TextView txtGender = (TextView)relLayout.findViewById(R.id.txtGender);
            txtGender.setText(business.GetGenderItem(card.getGenderEventId()-1));

            Button btnPartecipateRequest = (Button)relLayout.findViewById(R.id.btnPartecipateRequest);
            business.SetButtonRequest(btnPartecipateRequest,!card.IsRequestSubmitted());
            if ( business.checkButtonNotClickable(btnPartecipateRequest,card.getDateEvent()))
            {
                btnPartecipateRequest.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        onRequestPartecipate(v,card.getEventId());
                    }
                });
            }

            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v, card.getEventId());
                }
            });
            table.addView(row);
            existingCards.Add(card);
        }
        table.addView(row_progress);
    }

    protected boolean CardAlreadyExists(CardHome card)
    {
        return existingCards.GetCard(card.getEventId()) != null;
    }

    protected void onRequestPartecipate(View v, int EventId)
    {
        business.LoadingRequestButton(((Button)v),true);
        SendRequestPartecipate((Button)v,EventId);
    }

    @Override
    protected void onRowClick(View v, int EventId)
    {
        CardHome card = (CardHome) homeCards.GetCard(EventId);
        if(card != null)
        {
            SwipeDownOpenActivity(activity.getBaseContext(), CardDetailActivity.class, card);
        }
    }

}
