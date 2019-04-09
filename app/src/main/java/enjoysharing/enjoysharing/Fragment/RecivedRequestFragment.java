package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import enjoysharing.enjoysharing.Activity.CardDetailActivity;
import enjoysharing.enjoysharing.Activity.RequestListActivity;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Business.BusinessJSON;
import enjoysharing.enjoysharing.DataObject.Card.CardCollection;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestRecived;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestUserList;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestUserListCollection;
import enjoysharing.enjoysharing.R;

public class RecivedRequestFragment extends FragmentBase {

    protected TableLayout tableRecivedRequests;
    protected TextView txtConfirmDecline;
    protected Button btn1, btn2;
    protected boolean stateRequest;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_recived_request, container, false);
        tableRecivedRequests = (TableLayout) vMain.findViewById(R.id.tableRecivedRequests);
        setTableReloadScrollView((ScrollView)vMain.findViewById(R.id.tableRecievedRequestScrollView));
        reloadOnSwipeBottom = true;
        super.onCreateView(inflater,container,savedInstanceState);
        return vMain;
    }
    @Override
    protected boolean CheckForCurrentFragment() { return activity.getCurrentMenuPosition()==1; }
    @Override
    public void StartFragment()
    {
        business = new BusinessBase(activity);
        super.StartFragment();
        LoadTable();
    }

    protected CardRequestUserListCollection recivedRequestCards;
    protected CardCollection cardCollection;

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
        mTask.AddParameter("RequestType","RR");
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
    // Load with server call
    protected void AcceptRefuseAll(Button btnConfirmRequest, Button btnDeclineRequest, TextView txtConfirmDeclineRequest, int EventId, boolean state)
    {
        if (mTask != null) {
            SetButtonsVisibility(btnConfirmRequest,btnDeclineRequest,true);
            return;
        }
        //ShowProgress(true);
        PostCall = true;
        stateRequest = state;  // True = accetto, False = rifiuto
        txtConfirmDecline = txtConfirmDeclineRequest;
        btn1 = btnConfirmRequest;
        btn2 = btnDeclineRequest;
        SetButtonsVisibility(false);
        mTask = new FragmentRequestTask(true, false, "RequestServlet",false);
        mTask.AddParameter("RequestType",stateRequest?"AR":"RR");  // Accept Request or Refuse Request
        mTask.AddParameter("EventId",EventId);
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
            cardCollection = new BusinessJSON(activity).GetRequestRecivedCards(activity.retObj.getMessage());
            recivedRequestCards = business.GetGroupedCards(cardCollection);
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
                txtConfirmDecline.setText(stateRequest?R.string.txtConfirmedRequest:R.string.txtDeclinedRequest);
                txtConfirmDecline.setVisibility(View.VISIBLE);
            }
            else
            {
                if(recivedRequestCards != null)
                    // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
                    DrawCardsOnTable(recivedRequestCards,tableRecivedRequests);
            }
        }
        else
        {
            ShowShortMessage(activity.retObj.getMessage());
            if (PostCall)
            {
                SetButtonsVisibility(true);
            }
        }
    }

    // Used by requests tabs
    @Override
    protected void DrawCardsOnTable(CardRequestUserListCollection cards, TableLayout table)
    {
        super.DrawCardsOnTable(cards,table);
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(100);
        for (int i=0; i<cards.List().size(); i++) {
            final CardRequestUserList card = (CardRequestUserList)cards.List().get(i);
            if(CardAlreadyExists(card)) continue;
            final TableRow row = (TableRow) LayoutInflater.from(activity).inflate(R.layout.card_request_recived, null);
            LinearLayout relLayout = (LinearLayout)row.getChildAt(0);

            // row.getChildAt(0) è il relative layout che contiene tutti gli elementi
            TextView txtUserCard = (TextView)relLayout.findViewById(R.id.txtUserCard);
            txtUserCard.setWidth(txtUserTitleWidth);
            txtUserCard.setText(card.getUsernames());
            txtUserCard.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(activity.getBaseContext(),RequestListActivity.class, cardCollection.GetCard(card.getEventId()), true);
                }
            });

            TextView txtRecivedRequest = (TextView)relLayout.findViewById(R.id.txtRecivedRequest);
            // Set width based on screen percentage
            txtRecivedRequest.setWidth(txtUserTitleWidth);
            if(card.IsMultiUsers()) txtRecivedRequest.setText(R.string.txtMultiRequestRecived);

            TextView txtTitleCard = (TextView)relLayout.findViewById(R.id.txtTitleCard);
            // Set width based on screen percentage
            txtTitleCard.setWidth(txtUserTitleWidth);
            txtTitleCard.setText(card.getTitle());
            txtTitleCard.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open event detail
                    CardRequestRecived cardReq = (CardRequestRecived) cardCollection.GetCard(card.getEventId());
                    if(cardReq != null)
                    {
                        SwipeDownOpenActivity(activity.getBaseContext(), CardDetailActivity.class, cardReq);
                    }
                }
            });

            final Button btnConfirmRequest = (Button)relLayout.findViewById(R.id.btnConfirmRequest);
            final Button btnDeclineRequest = (Button)relLayout.findViewById(R.id.btnDeclineRequest);
            final TextView txtConfirmDeclineRequest = (TextView)relLayout.findViewById(R.id.txtConfirmDeclineRequest);
            btnConfirmRequest.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRequestConfirm(card.getEventId(),btnConfirmRequest, btnDeclineRequest, txtConfirmDeclineRequest);
                }
            });
            btnDeclineRequest.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRequestDecline(card.getEventId(),btnConfirmRequest, btnDeclineRequest, txtConfirmDeclineRequest);
                }
            });

            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v);
                }
            });

            table.addView(row);
            AddToExistingCards(card);
        }
        AddProgressToTable(table);
    }
    // Used to confirm request
    protected void onRequestConfirm(int EventId, Button btnConfirmRequest, Button btnDeclineRequest, TextView txtConfirmDeclineRequest)
    {
        // Server call
        AcceptRefuseAll(btnConfirmRequest, btnDeclineRequest, txtConfirmDeclineRequest, EventId, true);
    }
    // Used to decline request
    protected void onRequestDecline(int EventId, Button btnConfirmRequest, Button btnDeclineRequest, TextView txtConfirmDeclineRequest)
    {
        // Server call
        AcceptRefuseAll(btnConfirmRequest, btnDeclineRequest, txtConfirmDeclineRequest, EventId, false);
    }

    protected void SetButtonsVisibility(Button btnConfirmRequest, Button btnDeclineRequest,boolean visible)
    {
        btnConfirmRequest.setVisibility(visible?View.VISIBLE:View.GONE);
        btnDeclineRequest.setVisibility(visible?View.VISIBLE:View.GONE);
    }

    protected void SetButtonsVisibility(boolean visible)
    {
        btn1.setVisibility(visible?View.VISIBLE:View.GONE);
        btn2.setVisibility(visible?View.VISIBLE:View.GONE);
    }

}