package enjoysharing.enjoysharing.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.DataObject.CardBase;
import enjoysharing.enjoysharing.DataObject.CardMyEvent;
import enjoysharing.enjoysharing.R;

public class IUEventActivity extends BaseActivity {

    protected EditText txtTitleIUEvent;
    protected Spinner genderIUEvent;
    protected ImageButton imgBtnGender;
    protected EditText txtContentIUEvent;
    protected EditText txtNumberPerson;
    protected TextView txtUserIUEvent;
    protected int EventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(IUEventActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iuevent);

        business = new BusinessBase(IUEventActivity.this);

        // Toolbar user for back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_NewEvent);
        //toolbar.setOverflowIcon(getDrawable(R.drawable.ic_search_custom));
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        mFormView = findViewById(R.id.iuevent_form);
        mProgressView = findViewById(R.id.iuevent_progress);

        genderIUEvent = (Spinner) findViewById(R.id.genderIUEvent);
        imgBtnGender = (ImageButton) findViewById(R.id.imgBtnGender);
        // Adapter for textsize
        String[] items = business.GetGenderItems();
        ArrayAdapter<String> widgetModeAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, items);
        // Layout for list layout
        widgetModeAdapter.setDropDownViewResource(R.layout.spinner_item_list);
        genderIUEvent.setAdapter(widgetModeAdapter);
        genderIUEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                imgBtnGender.setImageResource(business.GetGenderIcon(position));
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

        txtTitleIUEvent = (EditText) findViewById(R.id.txtTitleIUEvent);
        txtContentIUEvent = (EditText) findViewById(R.id.txtContentIUEvent);
        // In case of update username remain because is MY EVENTS!
        txtUserIUEvent = (TextView) findViewById(R.id.txtUserIUEvent);
        txtUserIUEvent.setText(user.getUsername());

        txtNumberPerson = (EditText) findViewById(R.id.txtNumberPerson);

        Button btnCreatEvent = (Button) findViewById(R.id.btnCreatEvent);
        btnCreatEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveEvent();
            }
        });
        EventId = 0;
        // Check if is in Update
        LoadMyEventDetails();
    }
    // Used to load details of MY EVENT if present
    protected void LoadMyEventDetails()
    {
        Intent i = getIntent();
        CardBase cardBase = (CardBase)i.getSerializableExtra("CardPassed");
        if(cardBase != null && cardBase instanceof CardMyEvent)
        {
            final CardMyEvent card = (CardMyEvent) cardBase;
            isUpdate = true;
            EventId = card.getEventId();
            txtTitleIUEvent.setText(card.getTitle());
            txtContentIUEvent.setText(card.getContent());
            txtNumberPerson.setText(""+card.getMaxRequest());
            genderIUEvent.setSelection(card.getGenderEventId());
            txtNumberPerson.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(event.getRawX() <= txtNumberPerson.getTotalPaddingLeft()) {
                            // Open list of persons
                            OpenRequestList(IUEventActivity.this,RequestListActivity.class, card, true);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        if(!isUpdate)
            SwipeUpCloseActivity(IUEventActivity.this,HomeActivity.class);
        else
        {
            super.StandardOnBackPressed();
            overridePendingTransition(0, R.anim.activity_exit_to_top);
        }
    }
    // Used to store information event
    protected void SaveEvent()
    {
        if(CheckInformations())
        {
            showProgress(true);
            mTask = new RequestTask(true, false, "EventServlet");
            mTask.AddParameter("RequestType",isUpdate?"UE":"NE");
            mTask.AddParameter("EventId",EventId);
            mTask.AddParameter("Title",txtTitleIUEvent.getText());
            mTask.AddParameter("Description",txtContentIUEvent.getText());
            mTask.AddParameter("MaxRequests",txtNumberPerson.getText());
            mTask.AddParameter("GenderEventId",genderIUEvent.getSelectedItemId());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = df.format(Calendar.getInstance().getTime());
            mTask.AddParameter("DateEvent",date);
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
    }
    private boolean CheckInformations() {
        if (mTask != null) {
            return true;
        }

        // Reset errors.
        txtTitleIUEvent.setError(null);
        txtContentIUEvent.setError(null);
        txtNumberPerson.setError(null);

        // Store values to check
        String title = txtTitleIUEvent.getText().toString();
        String content = txtContentIUEvent.getText().toString();
        String numberPerson = txtNumberPerson.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for number of persons
        if (TextUtils.isEmpty(numberPerson)) {
            txtNumberPerson.setError(getString(R.string.error_field_required));
            focusView = txtNumberPerson;
            cancel = true;
        }
        // Check for content
        if (TextUtils.isEmpty(content)) {
            txtContentIUEvent.setError(getString(R.string.error_field_required));
            focusView = txtContentIUEvent;
            cancel = true;
        }
        // Check for title
        if (TextUtils.isEmpty(title)) {
            txtTitleIUEvent.setError(getString(R.string.error_field_required));
            focusView = txtTitleIUEvent;
            cancel = true;
        }
        if (cancel) {
            // form field with an error.
            focusView.requestFocus();
            return false;
        }
        return true;
    }
    // TODO
    // Do request call ONLY FOR UPDATE
    @Override
    public void DoInBackground()
    { }
    // TODO
    // Used to fill fields ONLY FOR UPDATE
    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess && retObj.isOkResponse())
            onBackPressed();
        else
            Toast.makeText(IUEventActivity.this,retObj.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
