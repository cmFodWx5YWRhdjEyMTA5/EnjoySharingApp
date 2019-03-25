package enjoysharing.enjoysharing.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Business.BusinessCallService;
import enjoysharing.enjoysharing.Business.BusinessImage;
import enjoysharing.enjoysharing.DataObject.Card.CardBase;
import enjoysharing.enjoysharing.DataObject.Card.CardCollection;
import enjoysharing.enjoysharing.DataObject.CurrentUser;
import enjoysharing.enjoysharing.DataObject.JSONServiceResponseOBJ;
import enjoysharing.enjoysharing.DataObject.Parameter;
import enjoysharing.enjoysharing.DataObject.ParameterCollection;
import enjoysharing.enjoysharing.R;

public class BaseActivity extends AppCompatActivity implements View.OnTouchListener {

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
    public JSONServiceResponseOBJ retObj;
    // Questa variabile la uso per le esecuzioni a server spento!
    public boolean simulateCall = false;
    // Usata da alcune activity per distinguere Post da Get nel result
    protected boolean PostCall;
    // Usata dal metodo OnTouch
    static final int MIN_DISTANCE = 5;
    private float downX, downY, upX, upY;
    // Used to enable/disable reload cards on swipe bottom
    protected boolean reloadOnSwipeBottom = false;
    protected int IndexCard;
    protected CardCollection existingCards;
    protected TableRow row_progress;
    protected ProgressBar row_progress_bar;
    protected ScrollView tableReloadScrollView;
    // Used to check current fragment
    protected int currentMenuPosition;
    protected Bitmap currentUserImage;
    public ParameterCollection userImageToLoad;
    public ParameterCollection userImageUpdateDate;

    protected void SetContext(Context context){ this.context = context; }

    public CurrentUser GetUser() { return user; }

    protected void setTableReloadScrollView(ScrollView table) { tableReloadScrollView = table; }

    public int getCurrentMenuPosition() { return currentMenuPosition; }

    public Bitmap GetCurrentUserImage()
    {
        if(user.getProfileImage() != null && !user.getProfileImage().equals(""))
        {
            if(currentUserImage == null)
                currentUserImage = business.StringToImage(user.getProfileImage());
            return currentUserImage;
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        user = new CurrentUser(this);
        user.LoadFromXMLFile();
        currentUserImage = null;
        if(reloadOnSwipeBottom)
        {
            tableReloadScrollView.getViewTreeObserver()
                    .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                        @Override
                        public void onScrollChanged() {
                            if (!tableReloadScrollView.canScrollVertically(1)) {
                                SetRowProgressVisibility(true);
                                LoadTable();
                            }
                            if (!tableReloadScrollView.canScrollVertically(-1)) {
                            }
                        }
                    });
        }
        InitReload();
        row_progress = (TableRow) LayoutInflater.from(this).inflate(R.layout.progress_bar, null);
        row_progress_bar = (ProgressBar) row_progress.findViewById(R.id.progress_bar);
    }
    // Used to expand/collapse VIEWS
    protected void expand(View view) {
        //set Visible
        view.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(view, 0, view.getMeasuredHeight());
        mAnimator.start();
    }

    protected void collapse(final View view) {
        int finalHeight = view.getHeight();
        ValueAnimator mAnimator = slideAnimator(view, finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) { }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
        mAnimator.start();
    }

    protected ValueAnimator slideAnimator(final View view, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    // Used to load bitmap image profile if exists
    public Bitmap LoadProfileImage(String userId)
    {
        String directory = Environment.getExternalStorageDirectory()+ File.separator + getString(R.string.app_name);
        String filename = "img_prof_"+userId+".jpg";
        try
        {
            File imagefile = new File(directory + File.separator + filename);
            if(imagefile.exists())
            {
                if(((Date)userImageUpdateDate.Get(userId)).after(new Date(imagefile.lastModified()))) return null;
                Bitmap bitmap = BitmapFactory.decodeFile(directory + File.separator + filename);
                return bitmap;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    // Used to store user image profile
    public void StoreImageProfile(String userId, Bitmap bitImage)
    {
        String directory = Environment.getExternalStorageDirectory()+ File.separator + getString(R.string.app_name);
        String filename = "img_prof_"+userId+".jpg";
        FileOutputStream outputStream;
        try {
            File folder = new File(directory);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File f = new File( directory + File.separator + filename);
            f.createNewFile();
            outputStream = new FileOutputStream(f);
            bitImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InsertImage(ImageView imageView, Bitmap bitImage)
    {
        business.LoadImage(imageView,bitImage);
    }

    // Used to add user for load image profile
    public void AddUserToLoadImage(CardBase card, ImageView imageView)
    {
        String userId = String.valueOf(card.getUserId());
        Date LastUpdateProfileImage = card.getLastUpdateProfileImage();
        AddUserToLoadImage(userId, LastUpdateProfileImage, imageView);
    }

    protected void AddUserToLoadImage(String userId, Date LastUpdateProfileImage, ImageView imageView)
    {
        if(userImageToLoad == null)
            userImageToLoad = new ParameterCollection();
        if(userImageUpdateDate == null)
            userImageUpdateDate = new ParameterCollection();
        if(userImageToLoad.Get(userId) == null)
        {
            userImageToLoad.Add(userId, new ArrayList<ImageView>());
            userImageUpdateDate.Add(userId, LastUpdateProfileImage);
        }
        ArrayList<ImageView> imageViews = (ArrayList<ImageView>)userImageToLoad.Get(userId);
        imageViews.add(imageView);
        userImageToLoad.Update(userId,imageViews);
    }

    public void LoadUserImages(BaseActivity activity)
    {
        if(userImageToLoad == null) return;
        for(Parameter param : userImageToLoad.GetParametersList())
        {
            BusinessImage bi = new BusinessImage(activity);
            bi.AddParameter("UserIdImage",""+param.GetName());
            bi.execute();
        }
    }

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
    public void SwipeDownOpenActivity(Context context, Class cl)
    {
        OpenActivityNoFinish(context,cl);
        overridePendingTransition(R.anim.activity_enter_from_top, 0);
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
    // Enter in Activity with swipe from left to right
    protected void SwipeLeftOpenActivity(Context context, Class cl)
    {
        OpenActivity(context,cl);
        overridePendingTransition(R.anim.activity_enter_from_left, R.anim.activity_exit_to_right);
    }
    // Back to Activity with swipe from left to right
    protected void SwipeCloseActivity(Context context, Class cl)
    {
        OpenActivity(context,cl);
        overridePendingTransition(R.anim.activity_enter_from_left, R.anim.activity_exit_to_right);
    }
    // Back to Activity with swipe from left to right
    protected void SwipeCloseRightActivity(Context context, Class cl)
    {
        OpenActivity(context,cl);
        overridePendingTransition(R.anim.activity_enter_from_right, R.anim.activity_exit_to_left);
    }
    // Back to Activity with swipe from Down to Up
    protected void SwipeUpCloseActivity(Context context, Class cl)
    {
        OpenActivity(context,cl);
        overridePendingTransition(0, R.anim.activity_exit_to_top);
    }
    // Back to Activity with swipe from Down to Up when called from startActivityForResult
    protected void SwipeUpCloseActivityFromResult(Context context, Class cl)
    {
        //setResult(Activity.RESULT_OK, intent);
        finish();
        overridePendingTransition(0, R.anim.activity_exit_to_top);
    }
    // Back to Activity with swipe from left to right when called from startActivityForResult
    protected void SwipeRightCloseActivityFromResult(Context context, Class cl)
    {
        //setResult(Activity.RESULT_OK, intent);
        finish();
        overridePendingTransition(0, R.anim.activity_exit_to_right);
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
    // Used to show a message with ToastMessage
    protected void ShowShortMessage(String message)
    {
        if(message == null || message.equals("")) return;
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    // Generally back to homepage
    @Override
    public void onBackPressed() {
        OpenActivity(context, HomeActivity.class);
    }
    // Used to close current activity as standard
    protected void StandardOnBackPressed() { super.onBackPressed(); }
    // Used to reset reload informations
    protected void InitReload()
    {
        IndexCard = 0;
        existingCards = new CardCollection();
    }
    // Used when load table
    protected void LoadTable()
    { }
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

    protected void SetRowProgressVisibility(boolean state)
    {
        row_progress_bar.setVisibility(state?View.VISIBLE:View.GONE);
    }
    // Used for click on rows
    protected void onRowClick(View v)
    { }
    // Used for click on rows
    protected void onRowClick(View v, int rowId)
    { }
    // Used for swipe rows (requests)
    public boolean BeforeSwipe() { return true; }
    public void onRightSwipe(View v, boolean wasLeft, int UserId){ }
    public void onLeftSwipe(View v, int UserId){ }
    // Used for click on request partecipate
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
    {
        if(reloadOnSwipeBottom)
            SetRowProgressVisibility(false);
    }

    protected void OnRequestCancelled()
    {
        mTask = null;
        showProgress(false);
    }

    // SWIPE
    protected void ActivityRightSwipe()
    { }

    protected void ActivityLeftSwipe()
    { }

    protected void ActivityDownSwipe()
    { }

    protected void ActivityUpSwipe(float deltaY)
    {
        // Se deltaY raggiunge un valore x attivo lo swipeUP
        if (deltaY >= 500)
            ActivityUpSwipeDone();
    }

    protected void ActivityUpSwipeDone()
    { }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) > Math.abs(deltaY))
                {
                    if(Math.abs(deltaX) > MIN_DISTANCE){
                        // left or right
                        if(deltaX < 0) {  this.ActivityRightSwipe(); return true; }
                        if(deltaX > 0 ) {  this.ActivityLeftSwipe(); return true; }
                    }
                    else {
                        return false; // We don't consume the event
                    }
                }
                // swipe vertical?
                else
                {
                    if(Math.abs(deltaY) > MIN_DISTANCE){
                        // top or down
                        if(deltaY < 0) { this.ActivityDownSwipe(); return true; }
                        if(deltaY > 0) {this.ActivityUpSwipe(deltaY); return true;}

                    }
                    else {
                        return false; // We don't consume the event
                    }
                }

                return true;
            }
            default: {
                return true;
            }
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    protected class RequestTask extends AsyncTask<Void, Void, Boolean> {

        protected BusinessCallService businessCallService;
        protected ParameterCollection params;
        protected String servletName;
        protected boolean useShowProgress = true;

        public void AddParameter(String name, Object value)
        {
            params.Add(name, value);
        }
        public void SetBitmap(String bitmapField, Bitmap bitmap) { businessCallService.SetBitmap(bitmapField, bitmap); }

        public RequestTask()
        {
            showProgress(true);
            params = new ParameterCollection();
            retObj = new JSONServiceResponseOBJ();
            businessCallService = null;
            AddParameter("V",getString(R.string.current_version));
        }

        public RequestTask(boolean executePost, boolean executeGet, String servletName)
        {
            this.useShowProgress = reloadOnSwipeBottom;
            if(useShowProgress)
                showProgress(true);
            params = new ParameterCollection();
            retObj = new JSONServiceResponseOBJ();
            this.servletName = servletName;
            businessCallService = new BusinessCallService(getString(R.string.service_url),servletName,user,executePost,executeGet);
            businessCallService.simulateCall = simulateCall;
            AddParameter("V",getString(R.string.current_version));
        }

        public RequestTask(boolean executePost, boolean executeGet, String servletName, boolean useShowProgress)
        {
            this.useShowProgress = useShowProgress;
            if(useShowProgress)
                showProgress(true);
            params = new ParameterCollection();
            retObj = new JSONServiceResponseOBJ();
            this.servletName = servletName;
            businessCallService = new BusinessCallService(getString(R.string.service_url),servletName,user,executePost,executeGet);
            businessCallService.simulateCall = simulateCall;
            AddParameter("V",getString(R.string.current_version));
        }

        public void SetLongTimeout() {
            if(businessCallService!=null)
                businessCallService.SetLongTimeout();
        }

        @Override
        protected Boolean doInBackground(Void... pars) {
            try
            {
                if(businessCallService!=null)
                {
                    businessCallService.SetParams(params);
                    businessCallService.Call();
                    retObj = businessCallService.retObj;
                }
                else
                {
                    retObj.setStateResponse(true);
                }
                DoInBackground();
            } catch (Exception e)
            {
                retObj.setMessage("GeneralError");
                retObj.setStateResponse(false);
            }
            return businessCallService!=null ? retObj.isOkResponse() : true;
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            requestSuccess = result;

            mTask = null;
            OnRequestPostExecute();
            if(useShowProgress)
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
