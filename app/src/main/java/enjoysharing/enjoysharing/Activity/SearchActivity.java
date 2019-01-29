package enjoysharing.enjoysharing.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.TooltipCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Business.BusinessJSON;
import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CardHome;
import enjoysharing.enjoysharing.R;

public class SearchActivity extends BaseActivity {

    protected EditText searchTo;
    protected EditText txtNumberPerson;
    protected LinearLayout barFilters;
    protected Spinner genderIUEvent;
    protected ImageButton imgBtnGender;
    // Used when filter bar open
    protected boolean useFilter = true;
    protected Button btn;
    protected boolean stateRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(SearchActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        business = new BusinessBase(SearchActivity.this);
        // Toolbar user for back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Search);
        //toolbar.setOverflowIcon(getDrawable(R.drawable.ic_search_custom));
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        searchTo = (EditText)findViewById(R.id.txtSearch);
        searchTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchCards();
            }
        });
        txtNumberPerson = (EditText) findViewById(R.id.txtNumberPerson);
        txtNumberPerson.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchCards();
            }
        });
        ImageButton imgBtnFilters = (ImageButton) findViewById(R.id.imgBtnFilters);
        barFilters = (LinearLayout) findViewById(R.id.barFilters);
        imgBtnFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(barFilters.getVisibility() == View.GONE)
                {
                    useFilter = false;
                    barFilters.setVisibility(View.VISIBLE);
                }
                else
                    barFilters.setVisibility(View.GONE);
            }
        });

        genderIUEvent = (Spinner) findViewById(R.id.genderIUEvent);
        imgBtnGender = (ImageButton) findViewById(R.id.imgBtnGender);
        // Adapter for textsize
        String[] items = business.GetGenderSearchItems();
        ArrayAdapter<String> widgetModeAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, items);
        // Layout for list layout
        widgetModeAdapter.setDropDownViewResource(R.layout.spinner_item_list);
        genderIUEvent.setAdapter(widgetModeAdapter);
        genderIUEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                imgBtnGender.setImageResource(business.GetGenderIconSearch(position));
                if(useFilter)
                    SearchCards();
                else
                    useFilter = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        });
        // Open DDL on click
        imgBtnGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderIUEvent.performClick();
            }
        });

        mFormView = findViewById(R.id.search_form);
        mProgressView = findViewById(R.id.search_progress);
        mFormView.requestFocus();
    }

    @Override
    public void onBackPressed() {
        SwipeCloseActivity(SearchActivity.this,HomeActivity.class);
    }

    protected void SearchCards()
    {
        if (mTask != null) {
            mTask.cancel(true);
        }
        //showProgress(true);
        PostCall = false;
        mTask = new RequestTask(false, true, "EventServlet");
        mTask.AddParameter("RequestType","S");
        mTask.AddParameter("Title",searchTo.getText());
        mTask.AddParameter("MaxRequest",txtNumberPerson.getText());
        mTask.AddParameter("GenderEventId",business.GetGenderSearchIndex(genderIUEvent.getSelectedItem().toString()));
        // TODO
        // Quando implemento le date uso la data come filtro
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String date = df.format(Calendar.getInstance().getTime());
//        mTask.AddParameter("DateEvent",date);
        mTask.execute();
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

    protected CardCollection searchCards;

    @Override
    public void DoInBackground()
    {
        if(!PostCall)
        {
            if (simulateCall) {
                searchCards = business.GetHomeCards(null);
                searchCards.FilterByTitle(searchTo.getText().toString());
                searchCards.FilterByNumberPerson(txtNumberPerson.getText().toString());
                // Il -1 è perchè nella lista search c'è un item in più (ovvero "tutti")
                searchCards.FilterByGender(business.GetGenderSearchIndex(genderIUEvent.getSelectedItem().toString()) - 1);
            } else
                searchCards = new BusinessJSON(SearchActivity.this).GetHomeCards(retObj.getMessage());
        }
    }

    @Override
    protected void OnRequestPostExecute()
    {

        if(requestSuccess && retObj.isOkResponse())
        {
            if(PostCall)
            {
                business.SetButtonRequest(btn,!stateRequest);
                business.LoadingRequestButton(btn,false);
            }
            else
            {
                if(searchCards != null)
                {
                    TableLayout searchTable = (TableLayout) findViewById(R.id.tblSearchCards);
                    // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
                    DrawCardsOnTable(searchCards, searchTable);
                }
            }
        }
        else
        {
            Toast.makeText(SearchActivity.this, retObj.getMessage(), Toast.LENGTH_SHORT).show();
            if(PostCall)
                business.LoadingRequestButton(btn,false);
        }
    }

    protected void DrawCardsOnTable(CardCollection cards, TableLayout table)
    {
        table.removeAllViews();
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(85);
        int parentTollerancePX = 5;
        for (int i=0; i<cards.List().size(); i++) {
            final CardHome card = (CardHome)cards.List().get(i);
            TableRow row = (TableRow) LayoutInflater.from(SearchActivity.this).inflate(R.layout.card_home, null);
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
            TextView txtNumberPerson = (TextView)relLayout.findViewById(R.id.txtNumberPerson);
            txtNumberPerson.setText(card.getAcceptedRequest() + "/" + card.getMaxRequest());
            txtNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(SearchActivity.this,RequestListActivity.class, card, false);
                }
            });
            ImageView imgBtnGender = (ImageView)relLayout.findViewById(R.id.imgBtnGender);
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderEventId()));
            TooltipCompat.setTooltipText(imgBtnGender, business.GetGenderItem(card.getGenderEventId()));

            Button btnPartecipateRequest = (Button)relLayout.findViewById(R.id.btnPartecipateRequest);
            business.SetButtonRequest(btnPartecipateRequest,true);
            btnPartecipateRequest.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRequestPartecipate(v,card.getEventId());
                }
            });

            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v, card.getEventId());
                }
            });
            table.addView(row);
        }
    }

    protected void onRequestPartecipate(View v, int EventId)
    {
        business.LoadingRequestButton(((Button)v),true);
        SendRequestPartecipate((Button)v,EventId);
    }

    @Override
    protected void onRowClick(View v, int EventId)
    {
        CardHome card = (CardHome) searchCards.GetCard(EventId);
        if(card != null)
        {
            SwipeDownOpenActivity(this, CardDetailActivity.class, card);
        }
    }
}
