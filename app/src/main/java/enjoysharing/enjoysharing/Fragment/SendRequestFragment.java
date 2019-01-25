package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.support.v7.widget.TooltipCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import enjoysharing.enjoysharing.Activity.CardDetailActivity;
import enjoysharing.enjoysharing.Activity.RequestListActivity;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CardRequest;
import enjoysharing.enjoysharing.R;

public class SendRequestFragment extends FragmentBase {

    protected TableLayout tableSendRequests;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_send_request, container, false);
        tableSendRequests = (TableLayout) vMain.findViewById(R.id.tableSendRequests);
        super.onCreateView(inflater,container,savedInstanceState);
        return vMain;
    }
    @Override
    public void StartFragment()
    {
        business = new BusinessBase(activity);
        LoadSendRequests();
    }

    protected CardCollection sendRequestCards;

    @Override
    protected void ShowProgress(boolean state)
    {
        activity.showProgress(state, formView, progressView);
    }
    // TODO
    // Load with server call
    protected void LoadSendRequests()
    {

        if (mTask != null) {
            sendRequestCards = new CardCollection();
            DrawCardsOnTable(sendRequestCards,tableSendRequests);
            return;
        }
        //ShowProgress(true);
        mTask = new FragmentRequestTask();
        mTask.execute();
    }
    // TODO
    // Server call
    @Override
    protected void DoInBackground()
    {
        sendRequestCards = business.GetRequestCards();
    }

    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess && activity.retObj.isOkResponse())
        {
            if(sendRequestCards != null)
                // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
                DrawCardsOnTable(sendRequestCards,tableSendRequests);
        }
        else
            Toast.makeText(activity,activity.retObj.getMessage(), Toast.LENGTH_SHORT).show();
    }

    // Used by requests tabs
    protected void DrawCardsOnTable(CardCollection cards, TableLayout table)
    {
        table.removeAllViews();
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(85);
        for (int i=0; i<cards.List().size(); i++) {
            final CardRequest card = (CardRequest)cards.List().get(i);
            TableRow row = (TableRow) LayoutInflater.from(activity).inflate(R.layout.card_request_send, null);
            LinearLayout relLayout = (LinearLayout)row.getChildAt(0);
            // row.getChildAt(0) è il relative layout che contiene tutti gli elementi
            TextView txtUserCardSendRequest = (TextView)relLayout.findViewById(R.id.txtUserCardSendRequest);
            // Set width based on screen percentage
            txtUserCardSendRequest.setWidth(txtUserTitleWidth);
            txtUserCardSendRequest.setText(card.getUserName());
            TextView txtTitleCardSendRequest = (TextView)relLayout.findViewById(R.id.txtTitleCardSendRequest);
            // Set width based on screen percentage
            txtTitleCardSendRequest.setWidth(txtUserTitleWidth);
            txtTitleCardSendRequest.setText(card.getTitle());
            TextView txtNumberPerson = (TextView)relLayout.findViewById(R.id.txtNumberPerson);
            txtNumberPerson.setText(card.getAcceptedRequest() + "/" + card.getMaxRequest());
            txtNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(activity.getBaseContext(),RequestListActivity.class, card, false);
                }
            });
            ImageView imgBtnGender = (ImageView)relLayout.findViewById(R.id.imgBtnGender);
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderEventId()));
            TooltipCompat.setTooltipText(imgBtnGender, business.GetGenderItem(card.getGenderEventId()));
            Button btnPartecipateSendRequest = (Button)relLayout.findViewById(R.id.btnPartecipateSendRequest);
            business.SetButtonRequest(btnPartecipateSendRequest,false);
            btnPartecipateSendRequest.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRequestPartecipate(v);
                }
            });
            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v, card.getCardId());
                }
            });
            table.addView(row);
        }
    }
    // Used for click on request partecipate
    // TODO
    // Manage click on request partecipate
    protected void onRequestPartecipate(View v)
    {
        business.DisableRequestButton((Button)v);
    }

    @Override
    protected void onRowClick(View v, int rowId)
    {
        CardRequest card = (CardRequest) sendRequestCards.GetCard(rowId);
        if(card != null)
        {
            SwipeDownOpenActivity(activity.getBaseContext(), CardDetailActivity.class, card);
        }
    }

}
