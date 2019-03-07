package enjoysharing.enjoysharing.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.regex.Pattern;
import enjoysharing.enjoysharing.Business.BusinessJSON;
import enjoysharing.enjoysharing.R;

public class SignOnActivity extends BaseActivity {

    protected EditText txtName;
    protected EditText txtSurname;
    protected EditText txtBoxEmail;
    protected EditText txtBoxPassword;
    protected EditText txtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);

        business = new BusinessJSON(SignOnActivity.this);

        txtName = findViewById(R.id.txtName);
        txtSurname = findViewById(R.id.txtSurname);
        txtBoxEmail = findViewById(R.id.txtBoxEmail);
        txtBoxPassword = findViewById(R.id.txtBoxPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);

        Button btnBackToLogin = (Button) findViewById(R.id.btnBackToLogin);
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btnRegisterConfirm = (Button) findViewById(R.id.btnRegisterConfirm);
        btnRegisterConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });

        mFormView = findViewById(R.id.sign_on_form_layout);
        mProgressView = findViewById(R.id.sign_on_progress);
        mFormView.requestFocus();
    }
    @Override
    public void onBackPressed() {
        ClearFields();
        SwipeCloseRightActivity(SignOnActivity.this, LoginActivity.class);
    }

    protected void RegisterUser()
    {
        if(CheckData())
        {
            user.setEmail(getString(R.string.register_email));
            user.setPassword(getString(R.string.register_password));
            mTask = new RequestTask(true, false, "UserServlet");
            mTask.SetLongTimeout();
            mTask.AddParameter("RequestType","RU");
            mTask.AddParameter("Name",txtName.getText().toString().trim());
            mTask.AddParameter("Surname",txtSurname.getText().toString().trim());
            mTask.AddParameter("RegisterEmail",txtBoxEmail.getText().toString().trim());
            mTask.AddParameter("RegisterPassword",business.encrypt(txtBoxPassword.getText().toString().trim()));
            try
            {
                mTask.execute();
            }
            catch (Exception e)
            {
                retObj.setStateResponse(false);
                user.Clear();
            }
        }
    }
    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess && retObj.isOkResponse())
        {
            ShowShortMessage(getString(R.string.register_success));
            onBackPressed();
        }
        else
            ShowShortMessage(retObj.getMessage());
        user.Clear();
    }

    protected boolean CheckData()
    {
        txtName.setError(null);
        txtSurname.setError(null);
        txtBoxEmail.setError(null);
        txtBoxPassword.setError(null);
        txtConfirmPassword.setError(null);

        String name = txtName.getText().toString().trim();
        String surname = txtSurname.getText().toString().trim();
        String email = txtBoxEmail.getText().toString().trim();
        String password = txtBoxPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            txtBoxPassword.setError(getString(R.string.error_password_required));
            focusView = txtBoxPassword;
            cancel = true;
        }
        else
            // Check for a valid password, if the user entered one.
            if (!isPasswordValid(password)) {
                txtBoxPassword.setError(getString(R.string.error_invalid_password));
                focusView = txtBoxPassword;
                cancel = true;
            }
        // Check for a valid confirm password.
        if (TextUtils.isEmpty(confirmPassword)) {
            txtConfirmPassword.setError(getString(R.string.error_confirm_password_required));
            focusView = txtConfirmPassword;
            cancel = true;
        }
        else
            // Check for a valid confirm password, if the user entered one.
            if (!isPasswordValid(confirmPassword)) {
                txtConfirmPassword.setError(getString(R.string.error_invalid_confirm_password));
                focusView = txtConfirmPassword;
                cancel = true;
            }
        if(!cancel)
        {
            if(!password.equals(confirmPassword))
            {
                txtConfirmPassword.setError(getString(R.string.error_confirm_password_not_match));
                focusView = txtConfirmPassword;
                cancel = true;
            }
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            txtBoxEmail.setError(getString(R.string.error_email_required));
            focusView = txtBoxEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            txtBoxEmail.setError(getString(R.string.error_invalid_email));
            focusView = txtBoxEmail;
            cancel = true;
        }
        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            txtName.setError(getString(R.string.error_name_required));
            focusView = txtName;
            cancel = true;
        }
        // Check for a valid surname.
        if (TextUtils.isEmpty(surname)) {
            txtSurname.setError(getString(R.string.error_surname_required));
            focusView = txtSurname;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    protected void ClearFields()
    {
        txtName.setError(null);
        txtSurname.setError(null);
        txtBoxEmail.setError(null);
        txtBoxPassword.setError(null);
        txtConfirmPassword.setError(null);


        txtName.setText("");
        txtSurname.setText("");
        txtBoxEmail.setText("");
        txtBoxPassword.setText("");
        txtConfirmPassword.setText("");
    }
}
