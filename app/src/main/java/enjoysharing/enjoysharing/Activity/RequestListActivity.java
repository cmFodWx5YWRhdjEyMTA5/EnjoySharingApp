package enjoysharing.enjoysharing.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Dictionary;
import java.util.HashMap;

import enjoysharing.enjoysharing.AdapterObject.CardSwipeDetector;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Business.BusinessJSON;
import enjoysharing.enjoysharing.DataObject.CardBase;
import enjoysharing.enjoysharing.DataObject.RequestUser;
import enjoysharing.enjoysharing.DataObject.UserCollection;
import enjoysharing.enjoysharing.R;

public class RequestListActivity extends BaseActivity {

    protected EditText txtSearch;
    protected TableLayout tblRequestList;
    protected boolean canManageList;
    protected CardBase cardPassed;
    protected boolean RequestStatus;
    protected boolean WasLeft;
    protected int UserId;
    protected HashMap<Integer, CardSwipeDetector> swipeListenerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(RequestListActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        business = new BusinessBase(RequestListActivity.this);

        // Toolbar user for back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_request_list);
        //toolbar.setOverflowIcon(getDrawable(R.drawable.ic_search_custom));
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        txtSearch = (EditText) findViewById(R.id.txtSearch);
        txtSearch.addTextChangedListener(new TextWatcher() {
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
                SearchUsers();
            }
        });

        mFormView = findViewById(R.id.form_request_list);
        mProgressView = findViewById(R.id.progress_request_list);

        Intent i = getIntent();
        canManageList = (boolean)i.getSerializableExtra("canManageList");
        cardPassed = (CardBase) i.getSerializableExtra("cardPassed");

        SearchUsers();
    }

    @Override
    public void onBackPressed() {
        super.StandardOnBackPressed();
        overridePendingTransition(0, R.anim.activity_exit_to_right);
    }

    protected void SearchUsers()
    {
        if (mTask != null) {
            return;
        }
        PostCall = false;
        //showProgress(true);
        mTask = new RequestTask(false, true, "RequestServlet");
        mTask.AddParameter("RequestType","R");
        mTask.AddParameter("EventId",cardPassed.getEventId());
        mTask.AddParameter("UserName",txtSearch.getText());
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

    protected void SetRequestStatus(boolean Status, boolean wasLeft, int UserId)  // true = accepted, false = refused
    {
        if (mTask != null) {
            mTask.cancel(true);
        }
        PostCall = true;
        RequestStatus = Status;
        this.UserId = UserId;
        WasLeft = wasLeft;
        //showProgress(true);
        mTask = new RequestTask(true, false, "RequestServlet",false);
        mTask.AddParameter("RequestType","URS");
        mTask.AddParameter("EventId",cardPassed.getEventId());
        mTask.AddParameter("UserId",UserId);
        mTask.AddParameter("Status",Status?1:3);  // 1 = Accepted, 3 = Refused
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

    protected UserCollection users;

    @Override
    public void DoInBackground()
    {
        if(!PostCall)
        {
            if(simulateCall)
            {
                users = business.GetRequestList(null);
                users.FilterByUsername(txtSearch.getText().toString());
            }
            else
                users = new BusinessJSON(RequestListActivity.this).GetRequestList(retObj.getMessage());
        }
    }

    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess && retObj.isOkResponse())
        {
            if(PostCall)
            {
                if(RequestStatus)  // Accept
                {
                    cardPassed.setAcceptedRequest(cardPassed.getAcceptedRequest()+1);
                    SetRequestInfo();
                    swipeListenerList.get(UserId).SetAccepted();
                }
                else   // Refuse
                {
                    if(WasLeft)
                    {
                        cardPassed.setAcceptedRequest(cardPassed.getAcceptedRequest()-1);
                    }
                    SetRequestInfo();
                    swipeListenerList.get(UserId).SetDecined();
                }
            }
            else
            {
                if(users != null)
                {
                    tblRequestList = (TableLayout) findViewById(R.id.tblRequestList);
                    // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
                    DrawCardsOnTable(users);
                }
            }
        }
        else
        {
            Toast.makeText(RequestListActivity.this,retObj.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void SetRequestInfo()
    {
        TextView txtMaxRequest = (TextView)findViewById(R.id.txtMaxRequest);
        TextView txtNumberPerson = (TextView)findViewById(R.id.txtNumberPerson);
        txtNumberPerson.setText(""+cardPassed.getAcceptedRequest());
        txtMaxRequest.setText("/"+cardPassed.getMaxRequest());
    }

    protected void DrawCardsOnTable(final UserCollection users)
    {
        SetRequestInfo();
        tblRequestList.removeAllViews();
        swipeListenerList = new HashMap<Integer,CardSwipeDetector>();
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(85);
        for (int i=0; i<users.List().size(); i++) {
            final RequestUser user = (RequestUser)users.List().get(i);
            TableRow row = (TableRow) LayoutInflater.from(RequestListActivity.this).inflate(R.layout.card_user_list, null);
            final LinearLayout linLayout = (LinearLayout)row.getChildAt(0);

            TextView txtUsername = (TextView)linLayout.findViewById(R.id.txtUsername);
            // Set width based on screen percentage
            txtUsername.setWidth(txtUserTitleWidth);
            txtUsername.setText(user.getUserName());

            ImageView imgUser = (ImageView) linLayout.findViewById(R.id.imgUser);

            CardSwipeDetector swipeListener = new CardSwipeDetector(RequestListActivity.this, imgUser, row);
            if(user.isAccepted()) swipeListener.SetAccepted();
            if(user.isDeclined()) swipeListener.SetDecined();
            swipeListener.CanManageList(canManageList);
            swipeListener.setUserId(user.getUserId());
            linLayout.setOnTouchListener(swipeListener);

            swipeListenerList.put(user.getUserId(),swipeListener);

            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v, user.getUserId());
                }
            });

            tblRequestList.addView(row);
        }
    }
    @Override
    public boolean BeforeSwipe()
    { return cardPassed.getAcceptedRequest() < cardPassed.getMaxRequest(); }
    @Override
    public void onRightSwipe(View v, boolean wasLeft, int UserId)
    {
        SetRequestStatus(false, wasLeft, UserId);
    }
    // Used when accept request
    @Override
    public void onLeftSwipe(View v, int UserId)
    {
        SetRequestStatus(true, false, UserId);
    }

    @Override
    protected void onRowClick(View v, int userId)
    {
        // TODO
        // When click on user open his/her profile
    }
}

