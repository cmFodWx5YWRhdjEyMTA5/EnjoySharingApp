package enjoysharing.enjoysharing.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.DataObject.Card.CardBase;
import enjoysharing.enjoysharing.DataObject.Card.CardMyEvent;
import enjoysharing.enjoysharing.R;

public class IUEventActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    protected EditText txtTitleIUEvent;
    protected Spinner genderIUEvent;
    protected ImageView imgBtnGender;
    protected EditText txtContentIUEvent;
    protected EditText txtNumberPerson;
    protected TextView txtUserIUEvent;
    protected TextView txtDateEvent;
    protected ImageButton imgBtnEventDate;
    //protected ImageButton imgBtnNumberPerson;
    protected LinearLayout layoutNumberPerson;
    protected LinearLayout layoutGender;
    protected TextView txtGender;
    protected int EventId;
    protected int year, month, day;

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
                imgBtnGender.setImageResource(business.GetGenderIcon(position+1));
                txtGender.setText(business.GetGenderItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        });
        layoutGender = (LinearLayout) findViewById(R.id.layoutGender);
        layoutGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderIUEvent.performClick();
            }
        });
        // Open DDL on click
        imgBtnGender = (ImageView) findViewById(R.id.imgBtnGender);
        imgBtnGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderIUEvent.performClick();
            }
        });
        txtGender = (TextView)findViewById(R.id.txtGender);

        txtTitleIUEvent = (EditText) findViewById(R.id.txtTitleIUEvent);
        txtContentIUEvent = (EditText) findViewById(R.id.txtContentIUEvent);
        // In case of update username remain because is MY EVENTS!
        txtUserIUEvent = (TextView) findViewById(R.id.txtUserIUEvent);
        txtUserIUEvent.setText(user.getUsername());

        txtNumberPerson = (EditText) findViewById(R.id.txtNumberPerson);
        layoutNumberPerson = (LinearLayout) findViewById(R.id.layoutNumberPerson);
        layoutNumberPerson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtNumberPerson.performClick();
            }
        });
        //imgBtnNumberPerson = (ImageButton) findViewById(R.id.imgBtnNumberPerson);

        imgBtnEventDate = (ImageButton) findViewById(R.id.imgBtnEventDate);
        imgBtnEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(IUEventActivity.this,IUEventActivity.this,
                        c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(c.getTimeInMillis());
                dpd.show();
            }
        });
        txtDateEvent = (TextView) findViewById(R.id.txtDateEvent);
        txtDateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgBtnEventDate.performClick();
            }
        });

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
        mFormView.requestFocus();
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
            genderIUEvent.setSelection(card.getGenderEventId()-1);
            txtGender.setText(business.GetGenderItem(card.getGenderEventId()-1));
            txtDateEvent.setText(business.GetDateString(card.getDateEvent()));
            /*imgBtnNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(IUEventActivity.this,RequestListActivity.class, card, true);
                }
            });*/
        }
    }
    @Override
    public void onBackPressed() {
        if(!isUpdate)
            SwipeRightCloseActivityFromResult(IUEventActivity.this,HomeActivity.class);
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
            mTask.AddParameter("Content",txtContentIUEvent.getText());
            mTask.AddParameter("MaxRequest",txtNumberPerson.getText());
            mTask.AddParameter("GenderEventId",business.GetGenderIndex(genderIUEvent.getSelectedItem().toString())+1);
            java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
        String eventDate = txtDateEvent.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for date
        if (TextUtils.isEmpty(eventDate)) {
            txtDateEvent.setError(getString(R.string.error_eventdate_required));
            focusView = imgBtnEventDate;
            cancel = true;
        }
        // Check for number of persons
        if (TextUtils.isEmpty(numberPerson)) {
            txtNumberPerson.setError(getString(R.string.error_maxrequest_required));
            focusView = txtNumberPerson;
            cancel = true;
        }
        // Check for content
        if (TextUtils.isEmpty(content)) {
            txtContentIUEvent.setError(getString(R.string.error_content_required));
            focusView = txtContentIUEvent;
            cancel = true;
        }
        // Check for title
        if (TextUtils.isEmpty(title)) {
            txtTitleIUEvent.setError(getString(R.string.error_title_required));
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
    // Used when set DATE
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month +1;
        this.day = dayOfMonth;
        Calendar c = Calendar.getInstance();
        TimePickerDialog tpd = new TimePickerDialog(IUEventActivity.this,IUEventActivity.this,
                c.get(Calendar.HOUR),c.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));
        tpd.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hourOfDay, minute);
        txtDateEvent.setText(business.GetDateString(c.getTime()));
    }
}
