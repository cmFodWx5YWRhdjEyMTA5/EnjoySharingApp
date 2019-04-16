package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

import enjoysharing.enjoysharing.Activity.CardDetailActivity;
import enjoysharing.enjoysharing.Activity.ErrorActivity;
import enjoysharing.enjoysharing.Activity.RequestListActivity;
import enjoysharing.enjoysharing.AdapterObject.CardSwipeDetector;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Business.BusinessJSON;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestRecived;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestUserList;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestUserListCollection;
import enjoysharing.enjoysharing.DataObject.Parameter;
import enjoysharing.enjoysharing.DataObject.ParameterCollection;
import enjoysharing.enjoysharing.DataObject.RequestUser;
import enjoysharing.enjoysharing.DataObject.UserCollection;
import enjoysharing.enjoysharing.R;

public class FriendsFragment extends FragmentBase {

    protected TableLayout tableFriends;
    protected EditText txtSearch;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_friends, container, false);
        tableFriends = (TableLayout) vMain.findViewById(R.id.tableFriends);
        txtSearch = (EditText) vMain.findViewById(R.id.txtSearch);
        setTableReloadScrollView((ScrollView)vMain.findViewById(R.id.tableFriendsScrollView));
        reloadOnSwipeBottom = true;
        setFormView(vMain.findViewById(R.id.swipeToRefresh));
        super.onCreateView(inflater,container,savedInstanceState);
        return vMain;
    }
    @Override
    protected boolean CheckForCurrentFragment() { return activity.getCurrentMenuPosition()==2; }

    @Override
    public void StartFragment()
    {
        business = new BusinessBase(activity);
        super.StartFragment();

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LoadTable();
            }
        });

        LoadTable();
    }
    // Load with server call
    @Override
    protected void LoadTable()
    {
        if (mTask != null) {
            return;
        }
        //ShowProgress(true);
        PostCall = false;
        mTask = new FragmentRequestTask(false, true, "UserServlet",false);
        mTask.AddParameter("RequestType","GF");  // Get Friends
        mTask.AddParameter("UserId",user.getUserId());
        mTask.AddParameter("UserName",txtSearch.getText());
        try
        {
            mTask.execute();
        }
        catch (Exception e)
        {
            activity.retObj.setStateResponse(false);
            activity.retObj.setMessage("GeneralError");
        }
    }

    protected UserCollection users;
    // Server call
    @Override
    protected void DoInBackground()
    {
        if(!PostCall)
        {
            users = new BusinessJSON(activity).GetRequestList(activity.retObj.getMessage());
        }
    }

    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess && activity.retObj.isOkResponse())
        {
            if(!PostCall)
            {
                if(users != null)
                {
                    // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
                    DrawCardsOnTable(users);
                }
            }
        }
        else
        {
            ShowShortMessage(activity.retObj.getMessage());
        }
    }

    protected void DrawCardsOnTable(final UserCollection users)
    {
        tableFriends.removeAllViews();
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(85);
        for (int i=0; i<users.List().size(); i++) {
            final RequestUser user = (RequestUser)users.List().get(i);

            final TableRow row = (TableRow) LayoutInflater.from(activity).inflate(R.layout.card_user_list, null);
            final LinearLayout linLayout = (LinearLayout)row.getChildAt(0);

            TextView txtUsername = (TextView)linLayout.findViewById(R.id.txtUsername);
            // Set width based on screen percentage
            txtUsername.setWidth(txtUserTitleWidth);
            txtUsername.setText(user.getUserName());

            ImageView imgUser = (ImageView) linLayout.findViewById(R.id.imgUser);
            AddUserToLoadImage(user.getUserId()+"",user.getLastUpdateProfileImage(),imgUser);

            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v, user.getUserId());
                }
            });

            tableFriends.addView(row);
        }
        LoadUserImages();
    }

    @Override
    protected void onRowClick(View v, int userId)
    {
        // TODO
        // When click on user open his/her profile
        //  ShowShortMessage("EH VOLEEEEVI! Dobbiamo ancora sviluppare questa parte");
        ParameterCollection params = new ParameterCollection();
        params.Add("ErrorString", "Errore Generico");
        OpenActivity(activity, ErrorActivity.class, params);
    }
}
