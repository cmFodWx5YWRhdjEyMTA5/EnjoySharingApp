package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
        View v = inflater.inflate(R.layout.fragment_send_request, container, false);
        business = new BusinessBase(activity);
        tableSendRequests = (TableLayout) v.findViewById(R.id.tableSendRequests);
        return v;
    }
    @Override
    public void StartFragment()
    {
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
        ShowProgress(true);
        mTask = new FragmentRequestTask();
        mTask.execute((Void) null);
    }
    // TODO
    // Server call
    @Override
    protected void DoInBackground()
    {
        sendRequestCards = new CardCollection();
        sendRequestCards.Add(new CardRequest("Utente 1","Titolo 1","Contenuto di prova 1 caricato da codice, proviamo a vedere come viene il testo sfumato in fondo alla text view...mah! Ho letto che non si può fare, però la credo difficile, stiamo a vedere!",null));
        sendRequestCards.Add(new CardRequest("Utente 2","Titolo 2","Contenuto di prova 2 caricato da codice",null));
        sendRequestCards.Add(new CardRequest("Utente 3","Titolo 3 moooooooooooollllllltttttoooooooo lllllluuuuuuunnnnnnngggggggggggooooooooo","Contenuto di prova 3 caricato da codice",null));
        sendRequestCards.Add(new CardRequest("Utente 4","Titolo 4","Contenuto di prova 4 caricato da codice",null));
    }

    @Override
    protected void OnRequestPostExecute()
    {
        // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
        DrawCardsOnTable(sendRequestCards,tableSendRequests);
    }

    // Used by requests tabs
    protected void DrawCardsOnTable(CardCollection cards, TableLayout table)
    {
        table.removeAllViews();
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(85);
        for (int i=0; i<cards.List().size(); i++) {
            CardRequest card = (CardRequest)cards.List().get(i);
            TableRow row = (TableRow) LayoutInflater.from(activity).inflate(R.layout.card_request_send, null);
            LinearLayout relLayout = (LinearLayout)row.getChildAt(0);
            // row.getChildAt(0) è il relative layout che contiene tutti gli elementi
            TextView txtUserCardSendRequest = (TextView)relLayout.findViewById(R.id.txtUserCardSendRequest);
            // Set width based on screen percentage
            txtUserCardSendRequest.setWidth(txtUserTitleWidth);
            txtUserCardSendRequest.setText(card.getUsername());
            TextView txtTitleCardSendRequest = (TextView)relLayout.findViewById(R.id.txtTitleCardSendRequest);
            // Set width based on screen percentage
            txtTitleCardSendRequest.setWidth(txtUserTitleWidth);
            txtTitleCardSendRequest.setText(card.getTitle());
            Button btnPartecipateSendRequest = (Button)relLayout.findViewById(R.id.btnPartecipateSendRequest);
            business.SetButtonRequest(btnPartecipateSendRequest,false);
            btnPartecipateSendRequest.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRequestPartecipate(v);
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
    // Used for click on request partecipate
    // TODO
    // Manage click on request partecipate
    protected void onRequestPartecipate(View v)
    {
        Button btn = (Button)v;
        btn.setEnabled(false);
        btn.setText(R.string.txtRequestPartecipateReversed);
        btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_request_disabled_custom,0,0,0);
        btn.setHint("0");
    }

}
