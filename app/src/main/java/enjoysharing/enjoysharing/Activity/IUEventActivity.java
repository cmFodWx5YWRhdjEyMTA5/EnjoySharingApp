package enjoysharing.enjoysharing.Activity;

import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import enjoysharing.enjoysharing.Business.BusinessCards;
import enjoysharing.enjoysharing.R;

public class IUEventActivity extends BaseHomeActivity {

    protected EditText txtTitleIUEvent;
    protected EditText txtContentIUEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(IUEventActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iuevent);

        business = new BusinessCards(IUEventActivity.this);

        CreateMenuElements();
        CreateButtons();
        imgBtnAdd.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.button_icon_home_selected_first) );
        navigationView.setNavigationItemSelectedListener(this);

        mFormView = findViewById(R.id.iuevent_form);
        mProgressView = findViewById(R.id.iuevent_progress);

        txtTitleIUEvent = (EditText) findViewById(R.id.txtTitleIUEvent);
        txtContentIUEvent = (EditText) findViewById(R.id.txtContentIUEvent);

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
        txtTitleIUEvent.setError(null);
        txtContentIUEvent.setError(null);

        // Store values at the time of the login attempt.
        String title = txtTitleIUEvent.getText().toString();
        String content = txtContentIUEvent.getText().toString();

        boolean cancel = false;
        View focusView = null;

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
