package enjoysharing.enjoysharing.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.R;

public class IUProfileActivity extends BaseActivity {

    protected EditText txtName;
    protected EditText txtSurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(IUProfileActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iuprofile);

        business = new BusinessBase(IUProfileActivity.this);

        // Toolbar user for back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        //toolbar.setOverflowIcon(getDrawable(R.drawable.ic_search_custom));
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        mFormView = findViewById(R.id.iuprofile_form);
        mProgressView = findViewById(R.id.iuprofile_progress);

        txtName = (EditText) findViewById(R.id.txtName);
        txtName.setText(user.getName());
        txtSurname = (EditText) findViewById(R.id.txtSurname);
        txtSurname.setText(user.getSurname());

        Button btnSaveProfile = (Button) findViewById(R.id.btnSaveProfile);
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveProfile();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        SwipeUpCloseActivityFromResult(IUProfileActivity.this,ProfileActivity.class);
    }

    // Used to store information profile
    protected void SaveProfile()
    {
        if(CheckInformations())
        {
            showProgress(true);
            mTask = new RequestTask(true, false, "UserServlet");
            mTask.AddParameter("RequestType","UP");
            mTask.AddParameter("Name",txtName.getText());
            mTask.AddParameter("Surname",txtSurname.getText());
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
        txtName.setError(null);
        txtSurname.setError(null);

        // Store values to check
        String name = txtName.getText().toString();
        String surname = txtSurname.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for name
        if (TextUtils.isEmpty(name)) {
            txtName.setError(getString(R.string.error_name_required));
            focusView = txtName;
            cancel = true;
        }
        // Check for surname
        if (TextUtils.isEmpty(surname)) {
            txtSurname.setError(getString(R.string.error_surname_required));
            focusView = txtSurname;
            cancel = true;
        }
        if (cancel) {
            // form field with an error.
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess && retObj.isOkResponse())
        {
            SaveUser();
            onBackPressed();
        }
        else
            ShowShortMessage(retObj.getMessage());
    }

    protected void SaveUser()
    {
        user.setName(txtName.getText().toString());
        user.setSurname(txtSurname.getText().toString());
        user.SaveOnXMLFile();
    }
}
