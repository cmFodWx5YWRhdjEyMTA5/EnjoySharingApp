package enjoysharing.enjoysharing.Activity;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import enjoysharing.enjoysharing.Business.BusinessJSON;
import enjoysharing.enjoysharing.DataObject.ParameterCollection;
import enjoysharing.enjoysharing.R;
import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    protected CheckBox chcBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        business = new BusinessJSON(LoginActivity.this);
        // Set up the login form.
        String email = user.getEmail();
        String pw = business.decrypt(user.getPassword());
        boolean rememberMe = user.getRememberMe();

        mEmailView = (AutoCompleteTextView) findViewById(R.id.txtBoxEmail);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.txtBoxPassword);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return attemptLogin();
                }
                return false;
            }
        });

        chcBoxRememberMe = (CheckBox) findViewById(R.id.chcBoxRememberMe);
        chcBoxRememberMe.setChecked(rememberMe);
        if(rememberMe)
        {
            mEmailView.setText(email);
            mPasswordView.setText(pw);
        }
        else
        {
            user.Clear();
            user.SaveOnXMLFile();
        }

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeLeftOpenActivity(LoginActivity.this, SignOnActivity.class);
            }
        });

        mFormView = findViewById(R.id.login_form_layout);
        mProgressView = findViewById(R.id.login_progress);
        mFormView.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up the login form.
        String email = user.getEmail();
        String pw = user.getPassword();

        if(email != null && email != "" && pw != null && pw != "")
            Login();
    }

    protected void Login()
    {
        if(attemptLogin())
        {
            CallLogin();
        }
        else
        {
            user.Clear();
            user.SaveOnXMLFile();
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean attemptLogin() {
        if (mTask != null) {
            return true;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_password_required));
            focusView = mPasswordView;
            cancel = true;
        }
        else
            // Check for a valid password, if the user entered one.
            if (!isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_email_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
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

    protected boolean CallLogin()
    {
        // First get email and password from edittext
        user.setEmail(mEmailView.getText().toString());
        // Crypto la password!
        user.setPassword(business.encrypt(mPasswordView.getText().toString()));
        // Setto il remember me
        user.setRememberMe(chcBoxRememberMe.isChecked());
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        //showProgress(true);
        mTask = new RequestTask(false, true, "UserServlet", true);
        mTask.SetLongTimeout();
        mTask.AddParameter("RequestType","LI");  // Log In
        finishOnPostExecute = true;
        try
        {
            mTask.execute();
            return true;
        }
        catch (Exception e)
        {
            retObj.setStateResponse(false);
            return false;
        }
    }
    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess && retObj.isOkResponse())
        {
            if(!simulateCall)
            {
                ParameterCollection params = business.GetParamsByJSON(retObj.getMessage());
                user.setUserId(Integer.parseInt(params.Get("UserId").toString()));
                user.setUsername(params.Get("UserName").toString());
                user.setName(params.Get("Name").toString());
                user.setSurname(params.Get("Surname").toString());
                user.setLastUpdateDatetimeProfileImage(params.Get("LastUpdateDatetimeProfileImage").toString());
            }
            else
            {
                user.setUsername("Utente Test");
                user.setName("Utente");
                user.setSurname("Test");
            }
            user.SaveOnXMLFile();
            SwipeOpenActivity(LoginActivity.this,HomeActivity.class);
        }
        else
        {
            user.Clear();
            user.SaveOnXMLFile();
            String message = retObj.getMessage();
            ShowShortMessage(message.equals("") ?"UserError":message);
        }
    }

    private boolean isEmailValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }
}

