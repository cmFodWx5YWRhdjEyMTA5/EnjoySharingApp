package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import enjoysharing.enjoysharing.Activity.CardDetailActivity;
import enjoysharing.enjoysharing.Activity.RequestListActivity;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Business.BusinessJSON;
import enjoysharing.enjoysharing.DataObject.Card.CardCollection;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestSent;
import enjoysharing.enjoysharing.R;

public class SendRequestFragment extends FragmentBase {

    protected TableLayout tableSendRequests;
    protected Button btn;
    protected int EventId;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_send_request, container, false);
        tableSendRequests = (TableLayout) vMain.findViewById(R.id.tableSendRequests);
        setFormView((FrameLayout) vMain.findViewById(R.id.main_frame_send_request));
        setTableReloadScrollView((ScrollView)vMain.findViewById(R.id.tableSendRequestScrollView));
        reloadOnSwipeBottom = true;
        super.onCreateView(inflater,container,savedInstanceState);
        return vMain;
    }
    @Override
    protected boolean CheckForCurrentFragment() { return parentFragment.CheckForCurrentFragment() && parentFragment.getCurrentMenuPosition()==1; }
    @Override
    public void StartFragment()
    {
        business = new BusinessBase(activity);
        if(tableSendRequests != null)
        {
            super.StartFragment();
            LoadTable();
        }
    }

    protected CardCollection sendRequestCards;

    @Override
    protected void ShowProgress(boolean state)
    {
        ShowProgressPassView(state);
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
        mTask = new FragmentRequestTask(false, true, "RequestServlet");
        mTask.AddParameter("RequestType","RS");
        mTask.AddParameter("UserId",user.getUserId());
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

    protected void DeactivateRequest(Button btnPartecipateSendRequest, int EventId)
    {
        if (mTask != null) {
            business.LoadingRequestButton(btnPartecipateSendRequest,false);
            return;
        }
        //ShowProgress(true);
        PostCall = true;
        btn = btnPartecipateSendRequest;
        this.EventId = EventId;
        mTask = new FragmentRequestTask(true, false, "RequestServlet",false);
        mTask.AddParameter("RequestType","DR");  // Deactivate Request
        mTask.AddParameter("EventId",EventId);
        mTask.AddParameter("UserId",user.getUserId());
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
    // Server call
    @Override
    protected void DoInBackground()
    {
        if(!PostCall)
        {
            sendRequestCards = new BusinessJSON(activity).GetRequestSentCards(activity.retObj.getMessage());
        }
    }

    @Override
    protected void OnRequestPostExecute()
    {
        super.OnRequestPostExecute();
        if(requestSuccess && activity.retObj.isOkResponse())
        {
            if(PostCall)
            {
                business.DisableRequestButton(btn);
                UpdateCard();
            }
            else
            {
                if(sendRequestCards != null)
                    // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
                    DrawCardsOnTable(sendRequestCards,tableSendRequests);
            }
        }
        else
        {
            ShowShortMessage(activity.retObj.getMessage());
            if (PostCall)
            {
                business.SetButtonRequest(btn,false);
            }
        }
    }

    protected void UpdateCard()
    {
        CardRequestSent card = (CardRequestSent) sendRequestCards.GetCard(EventId);
        if(card != null)
            card.setRequestSubmitted(false);
        EventId = 0;
    }

    // Used by requests tabs
    protected void DrawCardsOnTable(CardCollection cards, TableLayout table)
    {
        super.DrawCardsOnTable(cards,table);
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(87);
        int txtRequestStatusWidth = business.ConvertWidthBasedOnPerc(15);
        int parentTollerancePX = 5;
        for (int i=0; i<cards.List().size(); i++) {
            final CardRequestSent card = (CardRequestSent)cards.List().get(i);
            if(CardAlreadyExists(card)) continue;
            TableRow row = (TableRow) LayoutInflater.from(activity).inflate(R.layout.card_request_send, null);
            LinearLayout relLayout = (LinearLayout)row.getChildAt(0);

            // row.getChildAt(0) è il relative layout che contiene tutti gli elementi
            TextView txtUserCard = (TextView)relLayout.findViewById(R.id.txtUserCard);
            // Set width based on screen percentage
            txtUserCard.setWidth(txtUserTitleWidth);
            txtUserCard.setText(card.getUserName());
            txtUserCard.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    OpenUserProfile(card.getUserId());
                }
            });

            TextView txtTitleCard = (TextView)relLayout.findViewById(R.id.txtTitleCard);
            // Set the same width of parent - tollerance
            txtTitleCard.setWidth(((LinearLayout)txtTitleCard.getParent()).getWidth()-parentTollerancePX);
            txtTitleCard.setText(card.getTitle());

            // Set RequestStatus
            TextView txtRequestStatus = (TextView)relLayout.findViewById(R.id.txtRequestStatus);
            business.getRequestStatus(card.getRequestStatusId(), txtRequestStatus);

            ImageView imgUserCard = (ImageView)relLayout.findViewById(R.id.imgUserCard);
            AddUserToLoadImage(card,imgUserCard);
            imgUserCard.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    OpenUserProfile(card.getUserId());
                }
            });

            TextView txtNumberPerson = (TextView)relLayout.findViewById(R.id.txtNumberPerson);
            txtNumberPerson.setText(card.getAcceptedRequest() + "/" + card.getMaxRequest());

            final ImageButton imgBtnNumberPerson = (ImageButton) relLayout.findViewById(R.id.imgBtnNumberPerson);
            imgBtnNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(activity.getBaseContext(),RequestListActivity.class, card, false);
                }
            });

            LinearLayout layoutNumberPerson = (LinearLayout) relLayout.findViewById(R.id.layoutNumberPerson);
            layoutNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imgBtnNumberPerson.performClick();
                }
            });

            ImageView imgBtnGender = (ImageView)relLayout.findViewById(R.id.imgBtnGender);
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderEventId()));

            TextView txtGender = (TextView)relLayout.findViewById(R.id.txtGender);
            txtGender.setText(business.GetGenderItem(card.getGenderEventId()-1));

            Button btnPartecipateSendRequest = (Button)relLayout.findViewById(R.id.btnPartecipateSendRequest);
            card.setRequestSubmitted(true);
            business.SetButtonRequest(btnPartecipateSendRequest,!card.IsRequestSubmitted());
            btnPartecipateSendRequest.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRequestPartecipate(v, card.getEventId());
                }
            });

            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v, card.getEventId());
                }
            });

            table.addView(row);
            AddToExistingCards(card);
        }
        AddProgressToTable(table);
        LoadUserImages();
    }
    // Manage click on request partecipate
    protected void onRequestPartecipate(View v, int EventId)
    {
        business.LoadingRequestButton(((Button)v),true);
        super.StartFragment();
        DeactivateRequest((Button)v,EventId);
    }

    @Override
    protected void onRowClick(View v, int rowId)
    {
        CardRequestSent card = (CardRequestSent) sendRequestCards.GetCard(rowId);
        if(card != null)
        {
            SwipeDownOpenActivity(activity.getBaseContext(), CardDetailActivity.class, card);
        }
    }
    // TODO
    // Open user profile
    protected void OpenUserProfile(int userId)
    {
        ShowShortMessage("EH VOLEEEEVI! Dobbiamo ancora sviluppare questa parte");
    }

}
