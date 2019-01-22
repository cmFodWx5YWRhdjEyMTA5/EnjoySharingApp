package enjoysharing.enjoysharing.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Business.BusinessCallService;
import enjoysharing.enjoysharing.DataObject.CardBase;
import enjoysharing.enjoysharing.DataObject.CurrentUser;
import enjoysharing.enjoysharing.DataObject.JSONServiceResponseOBJ;
import enjoysharing.enjoysharing.DataObject.ParameterCollection;
import enjoysharing.enjoysharing.R;

public class BaseActivity extends AppCompatActivity {

    protected Context context;
    protected CurrentUser user;
    protected BusinessBase business;
    // Used to load menu elements
    protected Toolbar toolbar;
    protected DrawerLayout drawer;
    protected NavigationView navigationView;
    protected BottomNavigationView nav_menu_home;
    // Used to call server with requests
    protected RequestTask mTask = null;
    // Used to switch visibility on progress bar and main form
    protected View mProgressView;
    protected View mFormView;
    // Used to call finish method on post execute request
    protected boolean finishOnPostExecute = false;
    // Used to checkk if request success
    public boolean requestSuccess;
    // Used to understand if is update
    protected boolean isUpdate = false;
    // Used to retrieve result from service call
    protected JSONServiceResponseOBJ retObj;

    protected void SetContext(Context context){ this.context = context; }

    public CurrentUser GetUser() { return user; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        user = new CurrentUser(this);
        user.LoadFromXMLFile();
    }
    // Used when go back to an activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        OnReloadActivity();
    }
    // Used when activity reloaded
    protected void OnReloadActivity()
    { }
    // Used to open activity and pass Card
    protected void SwipeOpenActivity(Context context, Class cl, CardBase card)
    {
        OpenActivity(context,cl,card);
        overridePendingTransition(R.anim.activity_enter_from_right, R.anim.activity_exit_to_left);
    }
    // Switch Activity and pass Card
    public void OpenActivity(Context context, Class cl, CardBase card)
    {
        Intent intent = new Intent(context, cl);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("CardPassed", card);
        startActivityForResult(intent,1);
        overridePendingTransition(0,0);
        //finish();
    }
    // Enter in Activity with swipe from Up to Down
    public void SwipeDownOpenActivity(Context context, Class cl, CardBase card)
    {
        OpenActivity(context,cl, card);
        overridePendingTransition(R.anim.activity_enter_from_top, 0);
    }
    // Enter in Activity with swipe from right to left without finish current activity
    public void SwipeOpenActivityNoFinish(Context context, Class cl)
    {
        OpenActivityNoFinish(context,cl);
        overridePendingTransition(R.anim.activity_enter_from_right, R.anim.activity_exit_to_left);
    }
    // Enter in Activity with swipe from right to left
    protected void SwipeOpenActivity(Context context, Class cl)
    {
        OpenActivity(context,cl);
        overridePendingTransition(R.anim.activity_enter_from_right, R.anim.activity_exit_to_left);
    }
    // Back to Activity with swipe from left to right
    protected void SwipeCloseActivity(Context context, Class cl)
    {
        OpenActivity(context,cl);
        overridePendingTransition(R.anim.activity_enter_from_left, R.anim.activity_exit_to_right);
    }
    // Back to Activity with swipe from Down to Up
    protected void SwipeUpCloseActivity(Context context, Class cl)
    {
        OpenActivity(context,cl);
        overridePendingTransition(0, R.anim.activity_exit_to_top);
    }
    // Switch Activity
    protected void OpenActivity(Context context, Class cl)
    {
        Intent intent = new Intent(context, cl);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0,0);
        //finish();
    }
    // Switch Activity without close current
    protected void OpenActivityNoFinish(Context context, Class cl)
    {
        Intent intent = new Intent(context, cl);
        startActivityForResult(intent,1);
        overridePendingTransition(0,0);
        //finish();
    }
    // Used to open ListOfRequest
    public void OpenRequestList(Context context, Class cl, CardBase cardPassed, boolean canManageList)
    {
        // SwipeOpenActivityNoFinish
        // OpenActivityNoFinish + pass value + transition
        Intent intent = new Intent(context, cl);
        intent.putExtra("canManageList",canManageList);
        intent.putExtra("cardPassed",cardPassed);
        startActivityForResult(intent,1);
        overridePendingTransition(R.anim.activity_enter_from_right, R.anim.activity_exit_to_left);
    }

    // Generally back to homepage
    @Override
    public void onBackPressed() {
        OpenActivity(context, HomeActivity.class);
    }
    // Used to close current activity as standard
    protected void StandardOnBackPressed() { super.onBackPressed(); }
    // Used for click on rows
    protected void onRowClick(View v)
    { }
    // Used for click on rows
    protected void onRowClick(View v, int rowId)
    { }
    // Used for swipe rows (requests)
    public boolean BeforeSwipe() { return true; }
    public void onRightSwipe(View v, boolean wasLeft){ }
    public void onLeftSwipe(View v){ }
    // Used for click on request partecipate
    // TODO
    // Manage click on request partecipate
    protected void onRequestPartecipate(View v)
    {
        boolean state = ((Button)v).getHint() == "1";
        business.SetButtonRequest((Button)v,!state);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        showProgress(show, mFormView ,mProgressView);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show, final View formView, final View progressView) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        // ATTENZIONE
        // All'apertura dell'activity main, il tab home non viene caricato subito, quindi non avrei il mFormView
        // In questo caso lascio quello di default
        final View frame = formView == null ? mFormView : formView;
        final View progress = progressView == null ? mProgressView : progressView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            frame.setVisibility(show ? View.GONE : View.VISIBLE);
            frame.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    frame.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            frame.setVisibility(show ? View.GONE : View.VISIBLE);
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

        protected BusinessCallService businessCallService;
        protected ParameterCollection params;
        protected String servletName;

        public RequestTask()
        {
            params = new ParameterCollection();
            retObj = new JSONServiceResponseOBJ();
        }

        public RequestTask(boolean executePost, boolean executeGet, String servletName)
        {
            params = new ParameterCollection();
            retObj = new JSONServiceResponseOBJ();
            this.servletName = servletName;
            businessCallService = new BusinessCallService(getString(R.string.service_url),servletName,params,user,executePost,executeGet);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try
            {
                if(businessCallService!=null)
                {
                    businessCallService.Call();
                    retObj = businessCallService.retObj;
                }
                DoInBackground();
            } catch (Exception e)
            { return false; }
            return businessCallService!=null ? retObj.isOkResponse() : true;
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            requestSuccess = result;

            mTask = null;
            OnRequestPostExecute();
            showProgress(false);

            if (requestSuccess && finishOnPostExecute) {
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            OnRequestCancelled();
        }
    }

}
