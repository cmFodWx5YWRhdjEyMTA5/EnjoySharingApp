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

import enjoysharing.enjoysharing.AdapterObject.CardSwipeDetector;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.DataObject.CardBase;
import enjoysharing.enjoysharing.DataObject.RequestUser;
import enjoysharing.enjoysharing.DataObject.UserCollection;
import enjoysharing.enjoysharing.R;

public class RequestListActivity extends BaseActivity {

    protected EditText txtSearch;
    protected TableLayout tblRequestList;
    protected boolean canManageList;
    protected CardBase cardPassed;

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
            mTask.cancel(true);
        }
        showProgress(true);
        mTask = new RequestTask();
        mTask.execute((Void) null);
    }

    protected UserCollection users;

    @Override
    public void DoInBackground()
    {
        users = business.GetRequestList();
        users.FilterByUsername(txtSearch.getText().toString());
    }

    @Override
    protected void OnRequestPostExecute()
    {
        tblRequestList = (TableLayout) findViewById(R.id.tblRequestList);
        // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
        DrawCardsOnTable(users);
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
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(85);
        for (int i=0; i<users.List().size(); i++) {
            final RequestUser user = (RequestUser)users.List().get(i);
            TableRow row = (TableRow) LayoutInflater.from(RequestListActivity.this).inflate(R.layout.card_user_list, null);
            final LinearLayout linLayout = (LinearLayout)row.getChildAt(0);

            TextView txtUsername = (TextView)linLayout.findViewById(R.id.txtUsername);
            // Set width based on screen percentage
            txtUsername.setWidth(txtUserTitleWidth);
            txtUsername.setText(user.getUsername());

            ImageView imgUser = (ImageView) linLayout.findViewById(R.id.imgUser);

            CardSwipeDetector swipeListener = new CardSwipeDetector(RequestListActivity.this, imgUser, row);
            if(user.isAccepted()) swipeListener.SetAccepted();
            if(user.isDeclined()) swipeListener.SetDecined();
            swipeListener.CanManageList(canManageList);
            linLayout.setOnTouchListener(swipeListener);

            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v, user.getIdUser());
                }
            });

            tblRequestList.addView(row);
        }
    }
    @Override
    public boolean BeforeSwipe()
    { return cardPassed.getAcceptedRequest() < cardPassed.getMaxRequest(); }
    @Override
    public void onRightSwipe(View v, boolean wasLeft)
    {
        if(wasLeft)
        {
            cardPassed.setAcceptedRequest(cardPassed.getAcceptedRequest()-1);
        }
        SetRequestInfo();
    }
    // Used when accept request
    @Override
    public void onLeftSwipe(View v)
    {
        cardPassed.setAcceptedRequest(cardPassed.getAcceptedRequest()+1);
        SetRequestInfo();
    }

    @Override
    protected void onRowClick(View v, int userId)
    {
        // TODO
        // When click on user open his/her profile
    }
}

