package enjoysharing.enjoysharing.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import java.util.Date;
import enjoysharing.enjoysharing.Activity.BaseActivity;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Business.BusinessCallService;
import enjoysharing.enjoysharing.DataObject.Card.CardBase;
import enjoysharing.enjoysharing.DataObject.Card.CardCollection;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestUserListCollection;
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
    // Used to enable/disable reload cards on swipe bottom
    protected boolean reloadOnSwipeBottom = false;
    protected int IndexCard;
    protected CardCollection existingCards;
    protected TableRow row_progress;
    protected ProgressBar row_progress_bar;
    protected ScrollView tableReloadScrollView;
    protected int currentMenuPosition;
    protected FragmentBase parentFragment;

    protected View progressView;
    protected View formView;
    protected boolean mGlobalListenersAdded;

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

    protected void setTableReloadScrollView(ScrollView table) { tableReloadScrollView = table; }

    public int getCurrentMenuPosition() { return currentMenuPosition; }

    public void setParentFragment(FragmentBase parentFragment) { this.parentFragment = parentFragment; }
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
                    InitReload();
                    StartFragment();
                }
            });
        }
        mGlobalListenersAdded = false;
        InitReload();
        row_progress = (TableRow) LayoutInflater.from(activity).inflate(R.layout.progress_bar, null);
        row_progress_bar = (ProgressBar) row_progress.findViewById(R.id.progress_bar);
        StartFragment();
        return vMain;
    }
    // Used when activity reloaded
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ReloadFragment();
    }
    public void ReloadFragment() { StartFragment(); }

    protected void InitReload()
    {
        IndexCard = 0;
        existingCards = new CardCollection();
    }

    protected boolean CheckForCurrentFragment() { return true; }

    protected void SetRowProgressVisibility(boolean state)
    {
        row_progress_bar.setVisibility(state?View.VISIBLE:View.GONE);
    }

    protected boolean IsRowProgressVisible() { return row_progress_bar == null?false:row_progress_bar.getVisibility() == View.VISIBLE; }

    protected void OpenRequestList(Context context, Class cl, CardBase card, boolean canManageList)
    {
        activity.OpenRequestList(context, cl, card, canManageList);
    }
    // Enter in Activity with swipe from Up to Down
    protected void SwipeDownOpenActivity(Context context, Class cl)
    {
        activity.SwipeDownOpenActivity(context, cl);
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
    // Used to show a message with ToastMessage
    protected void ShowShortMessage(String message)
    {
        if(message == null || message.equals("")) return;
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
    // Creo metodo per customizzarlo dove serve
    protected void ShowProgress(boolean state)
    {
        if(!IsRowProgressVisible())
            activity.showProgress(state);
    }
    // Creo metodo per customizzarlo dove serve
    protected void ShowProgressPassView(boolean state)
    {
        if(!IsRowProgressVisible())
            activity.showProgress(state, formView, progressView);
    }
    // Used for functionality
    public void StartFragment()
    {
        if(!mGlobalListenersAdded && reloadOnSwipeBottom)
        {
            tableReloadScrollView.getViewTreeObserver()
                    .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                        @Override
                        public void onScrollChanged() {
                            if(CheckForCurrentFragment())
                            {
                                if (!tableReloadScrollView.canScrollVertically(1)) {
                                    SetRowProgressVisibility(true);
                                    LoadTable();
                                }
                                if (!tableReloadScrollView.canScrollVertically(-1)) {
                                }
                            }
                        }
                    });
            mGlobalListenersAdded = true;
        }
        InitReload();
    }
    // Used when load table
    protected void LoadTable()
    { }
    // Used to load table and reload on swipe bottom
    protected void DrawCardsOnTable(CardRequestUserListCollection cards, TableLayout table)
    {
        if(reloadOnSwipeBottom && IndexCard == 0)
        {
            table.removeAllViews();
        }
        else
        {
            table.removeView(row_progress);
        }
    }
    // Used to load table and reload on swipe bottom
    protected void DrawCardsOnTable(CardCollection cards, TableLayout table)
    {
        if(reloadOnSwipeBottom && IndexCard == 0)
        {
            table.removeAllViews();
        }
        else
        {
            table.removeView(row_progress);
        }
    }
    // Used to check if card already exists
    protected boolean CardAlreadyExists(CardBase card)
    {
        IndexCard++;
        return existingCards.GetCard(card.getEventId()) != null;
    }
    // Used to add user for load image profile
    protected void AddUserToLoadImage(CardBase card, ImageView imageView)
    {
        activity.AddUserToLoadImage(card,imageView);
    }
    // Used to add user for load image profile
    protected void AddUserToLoadImage(String userId, Date LastUpdateProfileImage, ImageView imageView)
    {
        activity.AddUserToLoadImage(userId, LastUpdateProfileImage, imageView);
    }
    // Used to add into card list
    protected void AddToExistingCards(CardBase card)
    {
        if(reloadOnSwipeBottom)
            existingCards.Add(card);
    }
    // Used to add row progress on table
    protected void AddProgressToTable(TableLayout table)
    {
        if(reloadOnSwipeBottom && existingCards.List().size()!=0)
            table.addView(row_progress);
    }
    // Used to load users profile images
    protected void LoadUserImages()
    {
        activity.LoadUserImages(activity);
    }
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
    {
        if(reloadOnSwipeBottom)
            SetRowProgressVisibility(false);
    }

    protected void OnRequestCancelled()
    {
        mTask = null;
        ShowProgress(false);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    protected class FragmentRequestTask extends AsyncTask<Void, Void, Boolean> {

        protected BusinessCallService businessCallService;
        protected ParameterCollection params;
        protected String servletName;
        protected boolean useShowProgress = true;

        public void AddParameter(String name, Object value)
        {
            params.Add(name, value);
        }
        public void SetBitmap(String bitmapField, Bitmap bitmap) { businessCallService.SetBitmap(bitmapField, bitmap); }

        public FragmentRequestTask()
        {
            ShowProgress(true);
            params = new ParameterCollection();
            activity.retObj = new JSONServiceResponseOBJ();
            businessCallService = null;
            AddParameter("V",activity.getString(R.string.current_version));
        }

        public FragmentRequestTask(boolean executePost, boolean executeGet, String servletName)
        {
            if(useShowProgress)
                ShowProgress(true);
            params = new ParameterCollection();
            activity.retObj = new JSONServiceResponseOBJ();
            this.servletName = servletName;
            businessCallService = new BusinessCallService(activity.getString(R.string.service_url),servletName,user,executePost,executeGet);
            AddParameter("V",activity.getString(R.string.current_version));
        }

        public FragmentRequestTask(boolean executePost, boolean executeGet, String servletName, boolean useShowProgress)
        {
            this.useShowProgress = useShowProgress;
            if(useShowProgress)
                ShowProgress(true);
            params = new ParameterCollection();
            activity.retObj = new JSONServiceResponseOBJ();
            this.servletName = servletName;
            businessCallService = new BusinessCallService(activity.getString(R.string.service_url),servletName,user,executePost,executeGet);
            AddParameter("V",activity.getString(R.string.current_version));
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
