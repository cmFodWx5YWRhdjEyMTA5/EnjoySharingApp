package enjoysharing.enjoysharing.Fragment;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import enjoysharing.enjoysharing.Activity.BaseActivity;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.DataObject.CurrentUser;
import enjoysharing.enjoysharing.R;

public class FragmentBase extends Fragment {

    protected CurrentUser user;
    protected BaseActivity activity;
    protected BusinessBase business;
    // Used to call server with requests
    protected FragmentRequestTask mTask = null;
    // Used to checkk if request success
    protected boolean requestSuccess;

    protected View progressView;
    protected FrameLayout formView;

    public void setProgressView(View progressView) {
        this.progressView = progressView;
    }

    public void setFormView(FrameLayout formView) {
        this.formView = formView;
    }

    public void setCurrentUser(CurrentUser user) {
        this.user = user;
    }

    public void SetActivity(BaseActivity activity) {
        this.activity = activity;
    }
    // Creo metodo per customizzarlo dove serve
    protected void ShowProgress(boolean state)
    {
        activity.showProgress(state);
    }
    // Used for functionality
    public void StartFragment(){ }
    // Used for click on rows
    protected void onRowClick(View v)
    { }
    // Used for click on request partecipate
    // TODO
    // Manage click on request partecipate
    protected void onRequestPartecipate(View v)
    {
        boolean state = ((Button)v).getHint() == "1";
        business.SetButtonRequest((Button)v,!state);
    }

    protected void DoInBackground()
    { }

    protected void OnRequestPostExecute()
    { }

    protected void OnRequestCancelled()
    {
        mTask = null;
        ShowProgress(false);
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    // TODO
    // To use Parameter Collection as input
    protected class FragmentRequestTask extends AsyncTask<Void, Void, Boolean> {

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
            activity.requestSuccess = requestSuccess;

            mTask = null;
            OnRequestPostExecute();
            ShowProgress(false);
        }

        @Override
        protected void onCancelled() {
            OnRequestCancelled();
        }
    }

}
