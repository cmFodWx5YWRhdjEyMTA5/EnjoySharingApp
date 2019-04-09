package enjoysharing.enjoysharing.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.DataObject.Card.CardBase;
import enjoysharing.enjoysharing.DataObject.Card.CardHome;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestSent;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestRecived;
import enjoysharing.enjoysharing.R;

public class CardDetailActivity extends BaseActivity {

    protected boolean isSendRequest = false;
    protected ImageView imgUserCard;
    protected TextView txtTitleCard;
    protected ImageView imgBtnGender;
    protected TextView txtContentCard;
    protected TextView txtNumberPerson;
    protected TextView txtUserCard;
    protected ImageButton imgBtnNumberPerson;
    protected LinearLayout layoutNumberPerson;
    protected TextView txtGender;
    protected TextView txtDateEvent;
    protected Button btnPartecipateRequest;
    protected Button btn;
    protected boolean stateRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(CardDetailActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        business = new BusinessBase(CardDetailActivity.this);

        // Toolbar user for back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_homedetail);
        //toolbar.setOverflowIcon(getDrawable(R.drawable.ic_search_custom));
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        mFormView = findViewById(R.id.homedetail_form);
        mFormView.setOnTouchListener(this);
        mProgressView = findViewById(R.id.homedetail_progress);

        imgUserCard = (ImageView) findViewById(R.id.imgUserCard);

        imgBtnGender = (ImageView) findViewById(R.id.imgBtnGender);
        txtGender = (TextView)findViewById(R.id.txtGender);

        txtTitleCard = (TextView) findViewById(R.id.txtTitleCard);
        txtContentCard = (TextView) findViewById(R.id.txtContentCard);
        txtDateEvent = (TextView) findViewById(R.id.txtDateEvent);

        txtUserCard = (TextView) findViewById(R.id.txtUserCard);

        txtNumberPerson = (TextView) findViewById(R.id.txtNumberPerson);
        imgBtnNumberPerson = (ImageButton) findViewById(R.id.imgBtnNumberPerson);
        layoutNumberPerson = (LinearLayout) findViewById(R.id.layoutNumberPerson);

        btnPartecipateRequest = (Button)findViewById(R.id.btnPartecipateRequest);
        // ATTENZIONE
        // Lasciare questo ordine di chiamate per il buon funzionamento!
        // Check if is home event
        LoadHomeEventDetails();
        // Check if is send request event
        LoadRequestEventDetails();
        // Check if is recived request event
        LoadRequestRecivedEventDetails();
        // Load user images if exists
        LoadUserImages(CardDetailActivity.this);
        mFormView.requestFocus();
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
        mTask = new RequestTask(true, false, "RequestServlet", false);
        mTask.AddParameter("RequestType",stateRequest?"NR":"DR");  // New Request or Delete Request
        mTask.AddParameter("EventId",EventId);
        mTask.AddParameter("UserId",user.getUserId());
        try
        {
            mTask.execute();
        }
        catch (Exception e)
        {
            retObj.setStateResponse(false);
            retObj.setMessage("GeneralError");
        }
    }

    protected void DeactivateRequest(Button btnPartecipateSendRequest, int EventId)
    {
        if (mTask != null) {
            business.LoadingRequestButton(btnPartecipateSendRequest,false);
            return;
        }
        //ShowProgress(true);
        PostCall = true;
        btn = btnPartecipateSendRequest;
        stateRequest = false;
        mTask = new RequestTask(true, false, "RequestServlet",false);
        mTask.AddParameter("RequestType","DR");  // Deactivate Request
        mTask.AddParameter("EventId",EventId);
        mTask.AddParameter("UserId",user.getUserId());
        try
        {
            mTask.execute();
        }
        catch (Exception e)
        {
            retObj.setStateResponse(false);
            retObj.setMessage("GeneralError");
        }
    }

    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess && retObj.isOkResponse())
        {
            if(PostCall)
            {
                if(isSendRequest)
                    business.DisableRequestButton(btn);
                else
                {
                    business.SetButtonRequest(btn,!stateRequest);
                    business.LoadingRequestButton(btn,false);
                }
            }
        }
        else
        {
            ShowShortMessage(retObj.getMessage());
            if(PostCall)
                business.LoadingRequestButton(btn,false);
        }
    }
    // Used to load details of HOME EVENT if present
    protected void LoadHomeEventDetails()
    {
        Intent i = getIntent();
        CardBase cardBase = (CardBase)i.getSerializableExtra("CardPassed");
        if(cardBase != null && cardBase instanceof CardHome)
        {
            final CardHome card = (CardHome) cardBase;
            AddUserToLoadImage(card,imgUserCard);
            txtUserCard.setText(card.getUserName());
            txtTitleCard.setText(card.getTitle());
            txtDateEvent.setText(business.GetDateString(card.getDateEvent()));
            txtContentCard.setText(card.getContent());
            txtNumberPerson.setText(card.getAcceptedRequest()+"/"+card.getMaxRequest());
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderEventId()));
            txtGender.setText(business.GetGenderItem(card.getGenderEventId()-1));
            business.SetButtonRequest(btnPartecipateRequest,!card.IsRequestSubmitted());
            btnPartecipateRequest.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRequestPartecipate(v,card.getEventId());
                }
            });
            imgBtnNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(CardDetailActivity.this,RequestListActivity.class, card, false);
                }
            });
            layoutNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imgBtnNumberPerson.performClick();
                }
            });
        }
    }
    // Used to load details of REQUEST EVENT RECIVED if present
    protected void LoadRequestRecivedEventDetails()
    {
        Intent i = getIntent();
        CardBase cardBase = (CardBase)i.getSerializableExtra("CardPassed");
        if(cardBase != null && cardBase instanceof CardRequestRecived)
        {
            isSendRequest = false;
            final CardRequestRecived card = (CardRequestRecived) cardBase;
            imgUserCard.setClipToOutline(true);
            business.LoadUserImage(imgUserCard);
            txtUserCard.setText(user.getUsername());
            txtTitleCard.setText(card.getTitle());
            txtDateEvent.setText(business.GetDateString(card.getDateEvent()));
            txtContentCard.setText(card.getContent());
            txtNumberPerson.setText(card.getAcceptedRequest()+"/"+card.getMaxRequest());
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderEventId()));
            txtGender.setText(business.GetGenderItem(card.getGenderEventId()-1));
            btnPartecipateRequest.setVisibility(View.INVISIBLE);
            imgBtnNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(CardDetailActivity.this,RequestListActivity.class, card, true);
                }
            });
            layoutNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imgBtnNumberPerson.performClick();
                }
            });
        }
    }
    // Used to load details of REQUEST EVENT SENT if present
    protected void LoadRequestEventDetails()
    {
        Intent i = getIntent();
        CardBase cardBase = (CardBase)i.getSerializableExtra("CardPassed");
        if(cardBase != null && cardBase instanceof CardRequestSent)
        {
            isSendRequest = true;
            final CardRequestSent card = (CardRequestSent) cardBase;
            AddUserToLoadImage(card,imgUserCard);
            txtUserCard.setText(card.getUserName());
            txtTitleCard.setText(card.getTitle());
            txtDateEvent.setText(business.GetDateString(card.getDateEvent()));
            txtContentCard.setText(card.getContent());
            txtNumberPerson.setText(card.getAcceptedRequest()+"/"+card.getMaxRequest());
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderEventId()));
            txtGender.setText(business.GetGenderItem(card.getGenderEventId()-1));
            if(card.IsRequestSubmitted())
                business.SetButtonRequest(btnPartecipateRequest,false);
            else
                business.DisableRequestButton(btnPartecipateRequest);
            btnPartecipateRequest.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRequestDeactivate(v,card.getEventId());
                }
            });
            imgBtnNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(CardDetailActivity.this,RequestListActivity.class, card, false);
                }
            });
            layoutNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imgBtnNumberPerson.performClick();
                }
            });
        }
    }

    protected void onRequestPartecipate(View v, int EventId)
    {
        business.LoadingRequestButton(((Button)v),true);
        SendRequestPartecipate((Button)v,EventId);
    }
    // Manage click on request partecipate
    protected void onRequestDeactivate(View v, int EventId)
    {
        business.LoadingRequestButton(((Button)v),true);
        DeactivateRequest((Button)v,EventId);
    }

    @Override
    protected void ActivityUpSwipeDone()
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        super.StandardOnBackPressed();
        overridePendingTransition(0, R.anim.activity_exit_to_top);
    }
}
