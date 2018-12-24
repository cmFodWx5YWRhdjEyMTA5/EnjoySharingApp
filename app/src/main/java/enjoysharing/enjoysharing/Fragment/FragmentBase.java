package enjoysharing.enjoysharing.Fragment;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;
import enjoysharing.enjoysharing.Activity.BaseActivity;
import enjoysharing.enjoysharing.Business.BusinessBase;

public class FragmentBase extends Fragment {

    protected BaseActivity activity;
    protected BusinessBase business;
    // Used to call server with requests
    protected FragmentRequestTask mTask = null;
    // Used to checkk if request success
    protected boolean requestSuccess;

    public void SetActivity(BaseActivity activity) {
        this.activity = activity;
    }
    // Used for click on rows
    public void onRowClick(View v)
    { }

    protected void DoInBackground()
    { }

    protected void OnRequestPostExecute()
    { }

    protected void OnRequestCancelled()
    {
        mTask = null;
        activity.showProgress(false);
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
            activity.showProgress(false);
        }

        @Override
        protected void onCancelled() {
            OnRequestCancelled();
        }
    }

}