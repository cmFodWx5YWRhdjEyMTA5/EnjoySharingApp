package enjoysharing.enjoysharing.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import enjoysharing.enjoysharing.AdapterObject.ActivitySwipeDetector;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.DataObject.CardBase;
import enjoysharing.enjoysharing.DataObject.RequestUser;
import enjoysharing.enjoysharing.DataObject.UserCollection;
import enjoysharing.enjoysharing.R;

public class RequestListActivity extends BaseActivity {

    protected EditText txtSearch;
    protected TableLayout tblRequestList;
    protected List<ActivitySwipeDetector> adapterList;
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
    protected void DoInBackground()
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
        txtNumberPerson.setText(""+cardPassed.getRequestNumber());
        txtMaxRequest.setText("/"+cardPassed.getMaxRequest());
    }

    protected void DrawCardsOnTable(final UserCollection users)
    {
        SetRequestInfo();
        tblRequestList.removeAllViews();
        adapterList = new ArrayList();
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(85);
        for (int i=0; i<users.List().size(); i++) {
            final RequestUser user = (RequestUser)users.List().get(i);
            TableRow row = (TableRow) LayoutInflater.from(RequestListActivity.this).inflate(R.layout.card_user_list, null);
            final LinearLayout linLayout = (LinearLayout)row.getChildAt(0);

            TextView txtUsername = (TextView)linLayout.findViewById(R.id.txtUsername);
            // Set width based on screen percentage
            txtUsername.setWidth(txtUserTitleWidth);
            txtUsername.setText(user.getUsername());

            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v, user.getIdUser());
                }
            });
            // TEST SWIPE
            ActivitySwipeDetector swipeListener = new ActivitySwipeDetector(RequestListActivity.this);
            swipeListener.SetOriginals(linLayout, canManageList && (cardPassed.getRequestNumber() < cardPassed.getMaxRequest()));
            adapterList.add(swipeListener);

            ViewTreeObserver observer = linLayout.getViewTreeObserver();

            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    linLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    // Do what you need with yourView here...

                    // Setto dopo perchè altrimenti mi sminchia tutte le righe
                    for (int i=0; i<users.List().size(); i++) {
                        final RequestUser user = (RequestUser)users.List().get(i);
                        TableRow row = (TableRow) tblRequestList.getChildAt(i);
                        LinearLayout linLayout = (LinearLayout)row.getChildAt(0);

                        ActivitySwipeDetector swipeListener = adapterList.get(i);
                        if(user.isAccepted()) swipeListener.SetAccepted(linLayout);
                        if(user.isDeclined()) swipeListener.SetDecined(linLayout);
                        linLayout.setOnTouchListener(swipeListener);
                    }
                }
            });

            tblRequestList.addView(row);
        }
    }
    // Used when refuse request
    @Override
    public boolean BeforeSwipe()
    { return cardPassed.getRequestNumber() < cardPassed.getMaxRequest(); }
    @Override
    public void onRightSwipe(View v, boolean wasLeft)
    {
        if(wasLeft)
        {
            cardPassed.setRequestNumber(cardPassed.getRequestNumber()-1);
        }
        SetRequestInfo();
    }
    // Used when accept request
    @Override
    public void onLeftSwipe(View v, boolean wasRight)
    {
        cardPassed.setRequestNumber(cardPassed.getRequestNumber()+1);
        SetRequestInfo();
    }

    @Override
    protected void onRowClick(View v, int userId)
    {
        // TODO
        // When click on user open his/her profile
    }
}

