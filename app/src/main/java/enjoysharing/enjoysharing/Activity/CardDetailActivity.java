package enjoysharing.enjoysharing.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.DataObject.CardBase;
import enjoysharing.enjoysharing.DataObject.CardHome;
import enjoysharing.enjoysharing.DataObject.CardRequest;
import enjoysharing.enjoysharing.DataObject.CardRequestRecived;
import enjoysharing.enjoysharing.R;

public class CardDetailActivity extends BaseActivity {

    protected boolean isSendRequest = false;
    protected TextView txtTitleHomeDetail;
    protected ImageView imgBtnGender;
    protected TextView txtContentHomeDetail;
    protected TextView txtNumberPerson;
    protected TextView txtUserHomeDetail;
    protected Button btnPartecipateRequest;

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
        mProgressView = findViewById(R.id.homedetail_progress);

        imgBtnGender = (ImageView) findViewById(R.id.imgBtnGender);

        txtTitleHomeDetail = (TextView) findViewById(R.id.txtTitleHomeDetail);
        txtContentHomeDetail = (TextView) findViewById(R.id.txtContentHomeDetail);

        txtUserHomeDetail = (TextView) findViewById(R.id.txtUserHomeDetail);

        txtNumberPerson = (TextView) findViewById(R.id.txtNumberPerson);

        btnPartecipateRequest = (Button)findViewById(R.id.btnPartecipateRequest);
        business.SetButtonRequest(btnPartecipateRequest,true);
        btnPartecipateRequest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onRequestPartecipate(v);
            }
        });
        // ATTENZIONE
        // Lasciare questo ordine di chiamate per il buon funzionamento!
        // Check if is home event
        LoadHomeEventDetails();
        // Check if is send request event
        LoadRequestEventDetails();
        // Check if is recived request event
        LoadRequestRecivedEventDetails();
        mFormView.requestFocus();
    }
    // Used to load details of HOME EVENT if present
    protected void LoadHomeEventDetails()
    {
        Intent i = getIntent();
        CardBase cardBase = (CardBase)i.getSerializableExtra("CardPassed");
        if(cardBase != null && cardBase instanceof CardHome)
        {
            final CardHome card = (CardHome) cardBase;
            txtUserHomeDetail.setText(card.getUserName());
            txtTitleHomeDetail.setText(card.getTitle());
            txtContentHomeDetail.setText(card.getContent());
            txtNumberPerson.setText(card.getAcceptedRequest()+"/"+card.getMaxRequest());
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderEventId()));
            txtNumberPerson.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        // Open list of persons
                        OpenRequestList(CardDetailActivity.this,RequestListActivity.class, card, false);
                        return true;
                    }
                    return false;
                }
            });
        }
    }// Used to load details of REQUEST EVENT RECIVED if present
    protected void LoadRequestRecivedEventDetails()
    {
        Intent i = getIntent();
        CardBase cardBase = (CardBase)i.getSerializableExtra("CardPassed");
        if(cardBase != null && cardBase instanceof CardRequestRecived)
        {
            isSendRequest = false;
            final CardRequestRecived card = (CardRequestRecived) cardBase;
            txtUserHomeDetail.setText(card.getUserName());
            txtTitleHomeDetail.setText(card.getTitle());
            txtContentHomeDetail.setText(card.getContent());
            txtNumberPerson.setText(card.getAcceptedRequest()+"/"+card.getMaxRequest());
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderEventId()));
            btnPartecipateRequest.setVisibility(View.INVISIBLE);
            txtNumberPerson.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        // Open list of persons
                        OpenRequestList(CardDetailActivity.this,RequestListActivity.class, card, true);
                        return true;
                    }
                    return false;
                }
            });
        }
    }
    // Used to load details of REQUEST EVENT SENT if present
    protected void LoadRequestEventDetails()
    {
        Intent i = getIntent();
        CardBase cardBase = (CardBase)i.getSerializableExtra("CardPassed");
        if(cardBase != null && cardBase instanceof CardRequest)
        {
            isSendRequest = true;
            final CardRequest card = (CardRequest) cardBase;
            txtUserHomeDetail.setText(card.getUserName());
            txtTitleHomeDetail.setText(card.getTitle());
            txtContentHomeDetail.setText(card.getContent());
            txtNumberPerson.setText(card.getAcceptedRequest()+"/"+card.getMaxRequest());
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderEventId()));
            business.SetButtonRequest(btnPartecipateRequest,false);
            txtNumberPerson.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        // Open list of persons
                        OpenRequestList(CardDetailActivity.this,RequestListActivity.class, card, false);
                        return true;
                    }
                    return false;
                }
            });
        }
    }
    // Used for click on request partecipate
    // TODO
    // Manage click on request partecipate
    protected void onRequestPartecipate(View v)
    {
        boolean state = ((Button)v).getHint() == "1";
        if(!isSendRequest)
            business.SetButtonRequest((Button)v,!state);
        else
            business.DisableRequestButton((Button)v);
    }
    @Override
    public void onBackPressed() {
        super.StandardOnBackPressed();
        overridePendingTransition(0, R.anim.activity_exit_to_top);
    }
}
