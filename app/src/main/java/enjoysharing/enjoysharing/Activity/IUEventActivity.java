package enjoysharing.enjoysharing.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.R;

public class IUEventActivity extends BaseActivity {

    protected Spinner genderIUEvent;
    protected ImageButton imgBtnGender;
    protected EditText txtContentIUEvent;
    protected EditText txtNumberPerson;

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

        txtContentIUEvent = (EditText) findViewById(R.id.txtContentIUEvent);
        // TODO
        // In case of UPDATE username will be fill based on event clicked
        TextView txtUserIUEvent = (TextView) findViewById(R.id.txtUserIUEvent);
        txtUserIUEvent.setText(user.getUsername());

        txtNumberPerson = (EditText) findViewById(R.id.txtNumberPerson);

        Button btnCreatEvent = (Button) findViewById(R.id.btnCreatEvent);
        btnCreatEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveEvent();
            }
        });
    }
    // Used to store information event
    protected void SaveEvent()
    {
        if(CheckInformations())
        {
            showProgress(true);
            mTask = new RequestTask();
            mTask.execute((Void) null);
        }
    }
    private boolean CheckInformations() {
        if (mTask != null) {
            return true;
        }

        // Reset errors.
        txtContentIUEvent.setError(null);
        txtNumberPerson.setError(null);
        // Only for try
        int genderPosition = business.GetGenderIndex(genderIUEvent.getSelectedItem().toString());

        // Store values at the time of the login attempt.
        String content = txtContentIUEvent.getText().toString();
        String numberPerson = txtNumberPerson.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for content
        if (TextUtils.isEmpty(content)) {
            txtContentIUEvent.setError(getString(R.string.error_field_required));
            focusView = txtContentIUEvent;
            cancel = true;
        }
        // Check for number of persons
        if (TextUtils.isEmpty(numberPerson)) {
            txtNumberPerson.setError(getString(R.string.error_field_required));
            focusView = txtNumberPerson;
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
    protected void DoInBackground()
    { }
    // TODO
    // Used to fill fields ONLY FOR UPDATE
    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess)
            SwipeCloseActivity(IUEventActivity.this,HomeActivity.class);
    }
}
