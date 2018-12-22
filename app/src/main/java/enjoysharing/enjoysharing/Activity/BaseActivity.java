package enjoysharing.enjoysharing.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import java.util.Objects;

import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.DataObject.CurrentUser;
import enjoysharing.enjoysharing.R;

public class BaseActivity extends AppCompatActivity {

    protected CurrentUser user;
    protected BusinessBase business;
    // Used to call server with requests
    protected RequestTask mTask = null;
    // Used to switch visibility on progress bar and main form
    protected View mProgressView;
    protected View mFormView;
    // Used to call finish method on post execute request
    protected boolean finishOnPostExecute = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        user = new CurrentUser(this);
        user.LoadFromXMLFile();
    }
    // Enter in Activity with swipe from left to right
    protected void SwipeOpenActivity(Context context, Class cl)
    {
        OpenActivity(context,cl);
        overridePendingTransition(R.anim.activity_enter_from_right, R.anim.activity_exit_to_left);
    }
    // Back to Activity with swipe from right to left
    protected void SwipeCloseActivity(Context context, Class cl)
    {
        OpenActivity(context,cl);
        overridePendingTransition(R.anim.activity_enter_from_left, R.anim.activity_exit_to_right);
    }
    // Switch Activity
    protected void OpenActivity(Context context, Class cl)
    {
        Intent intent = new Intent(context, cl);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    // Used for click on rows
    public void onRowClick(View v)
    { }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    protected void DoInBackground()
    { }

    protected void OnRequestPostExecute()
    { }

    protected void OnRequestCancelled()
    {
        mTask = null;
        showProgress(false);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    // TODO
        // To use Parameter Collection as input
    protected class RequestTask extends AsyncTask<Void, Void, Boolean> {

        protected boolean requestSuccess;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // TODO
                // Create get/post request with params based on parameter collection input
                // Simulate network access.
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return false;
            }

            DoInBackground();

            /*for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }*/
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            requestSuccess = success;

            mTask = null;
            OnRequestPostExecute();
            showProgress(false);

            if (success && finishOnPostExecute) {
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            OnRequestCancelled();
        }
    }

}
