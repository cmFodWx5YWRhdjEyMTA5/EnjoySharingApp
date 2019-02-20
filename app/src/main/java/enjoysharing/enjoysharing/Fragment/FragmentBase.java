package enjoysharing.enjoysharing.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import enjoysharing.enjoysharing.Activity.BaseActivity;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Business.BusinessCallService;
import enjoysharing.enjoysharing.DataObject.Card.CardBase;
import enjoysharing.enjoysharing.DataObject.CurrentUser;
import enjoysharing.enjoysharing.DataObject.JSONServiceResponseOBJ;
import enjoysharing.enjoysharing.DataObject.ParameterCollection;
import enjoysharing.enjoysharing.R;

public class FragmentBase extends Fragment {

    protected CurrentUser user;
    protected BaseActivity activity;
    protected BusinessBase business;
    protected View vMain;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    // Used to call server with requests
    protected FragmentRequestTask mTask = null;
    // Used to checkk if request success
    protected boolean requestSuccess;
    // Usata da alcuni fragment per distinguere Post da Get nel result
    protected boolean PostCall;

    protected View progressView;
    protected View formView;

    public void setProgressView(View progressView) {
        this.progressView = progressView;
    }

    public void setFormView(View formView) {
        this.formView = formView;
    }

    public void setCurrentUser(CurrentUser user) {
        this.user = user;
    }

    public void SetActivity(BaseActivity activity) {
        this.activity = activity;
    }

    // Lo creo solo per il refresh
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(vMain == null)
            vMain = inflater.inflate(R.layout.fragment_home, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) vMain.findViewById(R.id.swipeToRefresh);
        if(mSwipeRefreshLayout != null)
        {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Quando faccio swipe in alto refresho la pagina MA NON MOSTRO IL LOADING perchè ho il mio custom!
                    mSwipeRefreshLayout.setRefreshing(false);
                    StartFragment();
                }
            });
        }
        return vMain;
    }

    protected void OpenRequestList(Context context, Class cl, CardBase card, boolean canManageList)
    {
        activity.OpenRequestList(context, cl, card, canManageList);
    }
    // Enter in Activity with swipe from Up to Down
    protected void SwipeDownOpenActivity(Context context, Class cl, CardBase card)
    {
        activity.SwipeDownOpenActivity(context, cl, card);
    }
    // Switch Activity
    protected void OpenActivity(Context context, Class cl, CardBase card)
    {
        activity.OpenActivity(context, cl, card);
        //finish();
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
    // Used for click on rows
    protected void onRowClick(View v, int rowId)
    { }
    // Used for click on request partecipate
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

        protected BusinessCallService businessCallService;
        protected ParameterCollection params;
        protected String servletName;
        protected boolean useShowProgress = true;

        public void AddParameter(String name, Object value)
        {
            params.Add(name, value);
        }

        public FragmentRequestTask()
        {
            ShowProgress(true);
            params = new ParameterCollection();
            activity.retObj = new JSONServiceResponseOBJ();
            businessCallService = null;
        }

        public FragmentRequestTask(boolean executePost, boolean executeGet, String servletName)
        {
            if(useShowProgress)
                ShowProgress(true);
            params = new ParameterCollection();
            activity.retObj = new JSONServiceResponseOBJ();
            this.servletName = servletName;
            businessCallService = new BusinessCallService(activity.getString(R.string.service_url),servletName,user,executePost,executeGet);
            businessCallService.simulateCall = activity.simulateCall;
        }

        public FragmentRequestTask(boolean executePost, boolean executeGet, String servletName, boolean useShowProgress)
        {
            if(useShowProgress)
                ShowProgress(true);
            this.useShowProgress = useShowProgress;
            params = new ParameterCollection();
            activity.retObj = new JSONServiceResponseOBJ();
            this.servletName = servletName;
            businessCallService = new BusinessCallService(activity.getString(R.string.service_url),servletName,user,executePost,executeGet);
            businessCallService.simulateCall = activity.simulateCall;
        }

        @Override
        protected Boolean doInBackground(Void... pars) {
            try
            {
                if(businessCallService!=null)
                {
                    businessCallService.SetParams(params);
                    businessCallService.Call();
                    activity.retObj = businessCallService.retObj;
                }
                else
                {
                    activity.retObj.setStateResponse(true);
                }
                DoInBackground();
            } catch (Exception e)
            {
                activity.retObj.setMessage("GeneralError");
                activity.retObj.setStateResponse(false);
            }
            return businessCallService!=null ? activity.retObj.isOkResponse() : true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            requestSuccess = success;
            activity.requestSuccess = requestSuccess;

            mTask = null;
            OnRequestPostExecute();
            if(useShowProgress)
                ShowProgress(false);
        }

        @Override
        protected void onCancelled() {
            OnRequestCancelled();
        }
    }

}
