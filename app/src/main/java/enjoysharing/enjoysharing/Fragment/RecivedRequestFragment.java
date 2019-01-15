package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import enjoysharing.enjoysharing.Activity.CardDetailActivity;
import enjoysharing.enjoysharing.Activity.RequestListActivity;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CardRequestRecived;
import enjoysharing.enjoysharing.DataObject.CardRequestUserList;
import enjoysharing.enjoysharing.DataObject.CardRequestUserListCollection;
import enjoysharing.enjoysharing.R;

public class RecivedRequestFragment extends FragmentBase {

    protected TableLayout tableRecivedRequests;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_recived_request, container, false);
        business = new BusinessBase(activity);
        tableRecivedRequests = (TableLayout) vMain.findViewById(R.id.tableRecivedRequests);
        super.onCreateView(inflater,container,savedInstanceState);
        return vMain;
    }
    @Override
    public void StartFragment()
    {
        LoadRecivedRequests();
    }

    protected CardRequestUserListCollection recivedRequestCards;
    protected CardCollection cardCollection;

    @Override
    protected void ShowProgress(boolean state)
    {
        activity.showProgress(state, formView, progressView);
    }
    // TODO
    // Load with server call
    protected void LoadRecivedRequests()
    {

        if (mTask != null) {
            recivedRequestCards = new CardRequestUserListCollection();
            DrawCardsOnTable(recivedRequestCards,tableRecivedRequests);
            return;
        }
        ShowProgress(true);
        mTask = new FragmentRequestTask();
        mTask.execute((Void) null);
    }
    // TODO
    // Server call
    @Override
    protected void DoInBackground()
    {
        cardCollection = business.GetRequestRecivedCards();
        recivedRequestCards = business.GetGroupedCards(cardCollection);
    }

    @Override
    protected void OnRequestPostExecute()
    {
        // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
        DrawCardsOnTable(recivedRequestCards,tableRecivedRequests);
    }

    // Used by requests tabs
    protected void DrawCardsOnTable(CardRequestUserListCollection cards, TableLayout table)
    {
        table.removeAllViews();
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(100);
        for (int i=0; i<cards.List().size(); i++) {
            final CardRequestUserList card = (CardRequestUserList)cards.List().get(i);
            final TableRow row = (TableRow) LayoutInflater.from(activity).inflate(R.layout.card_request_recived, null);
            LinearLayout relLayout = (LinearLayout)row.getChildAt(0);
            // row.getChildAt(0) è il relative layout che contiene tutti gli elementi
            TextView txtUserRecivedRequest = (TextView)relLayout.findViewById(R.id.txtUserRecivedRequest);
            txtUserRecivedRequest.setWidth(txtUserTitleWidth);
            txtUserRecivedRequest.setText(card.getUsernames());
            txtUserRecivedRequest.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(activity.getBaseContext(),RequestListActivity.class, cardCollection.GetCard(card.getIdCardEvent()), true);
                }
            });
            TextView txtRecivedRequest = (TextView)relLayout.findViewById(R.id.txtRecivedRequest);
            // Set width based on screen percentage
            txtRecivedRequest.setWidth(txtUserTitleWidth);
            if(card.IsMultiUsers()) txtRecivedRequest.setText(R.string.txtMultiRequestRecived);
            TextView txtTitleRecivedRequest = (TextView)relLayout.findViewById(R.id.txtTitleRecivedRequest);
            // Set width based on screen percentage
            txtTitleRecivedRequest.setWidth(txtUserTitleWidth);
            txtTitleRecivedRequest.setText(card.getTitle());
            txtTitleRecivedRequest.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open event detail
                    CardRequestRecived cardReq = (CardRequestRecived) cardCollection.GetCard(card.getIdCardEvent());
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
                    onRequestConfirm(btnConfirmRequest, btnDeclineRequest, txtConfirmDeclineRequest);
                }
            });
            btnDeclineRequest.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRequestDecline(btnConfirmRequest, btnDeclineRequest, txtConfirmDeclineRequest);
                }
            });
            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v);
                }
            });
            table.addView(row);
        }
    }
    // Used to confirm request
    // TODO
    // Server call
    protected void onRequestConfirm(Button btnConfirmRequest, Button btnDeclineRequest, TextView txtConfirmDeclineRequest)
    {
        // Server call
        onRequestActionClick(btnConfirmRequest, btnDeclineRequest);
        txtConfirmDeclineRequest.setText(R.string.txtConfirmedRequest);
        txtConfirmDeclineRequest.setVisibility(View.VISIBLE);
    }
    // Used to decline request
    // TODO
    // Server call
    protected void onRequestDecline(Button btnConfirmRequest, Button btnDeclineRequest, TextView txtConfirmDeclineRequest)
    {
        // Server call
        onRequestActionClick(btnConfirmRequest, btnDeclineRequest);
        txtConfirmDeclineRequest.setText(R.string.txtDeclinedRequest);
        txtConfirmDeclineRequest.setVisibility(View.VISIBLE);
    }
    // Used to disable buttons
    protected void onRequestActionClick(Button btnConfirmRequest, Button btnDeclineRequest)
    {
        btnConfirmRequest.setVisibility(View.GONE);
        btnDeclineRequest.setVisibility(View.GONE);
    }

}