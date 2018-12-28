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
import enjoysharing.enjoysharing.DataObject.CardHome;
import enjoysharing.enjoysharing.R;

public class HomeFragment extends FragmentBase {

    protected TableLayout tableHomeCards;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        tableHomeCards = (TableLayout) v.findViewById(R.id.tableHomeCards);
        business = new BusinessBase(activity);
        setFormView((FrameLayout) v.findViewById(R.id.main_frame_home));
        return v;
    }
    @Override
    protected void ShowProgress(boolean state)
    {
        activity.showProgress(state, formView, progressView);
    }
    @Override
    public void StartFragment()
    {
        FillHomeCards();
    }

    protected CardCollection homeCards;
    // User to load home cards
    // TODO
    // Fill cards by server call
    protected void FillHomeCards()
    {
        if (mTask != null) {
            homeCards = new CardCollection();
            DrawCardsOnTable(homeCards,tableHomeCards);
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
        homeCards = new CardCollection();
        homeCards.Add(new CardHome("Utente 1","Titolo 1","Contenuto di prova 1 caricato da codice, proviamo a vedere come viene il testo sfumato in fondo alla text view...mah! Ho letto che non si può fare, però la credo difficile, stiamo a vedere!",null));
        homeCards.Add(new CardHome("Utente 2","Titolo 2","Contenuto di prova 2 caricato da codice",null));
        homeCards.Add(new CardHome("Utente 3","Titolo 3 moooooooooooollllllltttttoooooooo lllllluuuuuuunnnnnnngggggggggggooooooooo","Contenuto di prova 3 caricato da codice",null));
        homeCards.Add(new CardHome("Utente 4","Titolo 4","Contenuto di prova 4 caricato da codice",null));
    }

    @Override
    protected void OnRequestPostExecute()
    {
        // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
        DrawCardsOnTable(homeCards,tableHomeCards);
    }

    protected void DrawCardsOnTable(CardCollection cards, TableLayout table)
    {
        table.removeAllViews();
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(85);
        int parentTollerancePX = 5;
        for (int i=0; i<cards.List().size(); i++) {
            CardHome card = (CardHome)cards.List().get(i);
            TableRow row = (TableRow) LayoutInflater.from(activity).inflate(R.layout.card_home, null);
            LinearLayout relLayout = (LinearLayout)row.getChildAt(0);
            // row.getChildAt(0) è il relative layout che contiene tutti gli elementi
            TextView txtUserCardHome = (TextView)relLayout.findViewById(R.id.txtUserCardHome);
            // Set width based on screen percentage
            txtUserCardHome.setWidth(txtUserTitleWidth);
            txtUserCardHome.setText(card.getUsername());
            TextView txtTitleCardHome = (TextView)relLayout.findViewById(R.id.txtTitleCardHome);
            // Set width based on screen percentage
            txtTitleCardHome.setWidth(txtUserTitleWidth);
            txtTitleCardHome.setText(card.getTitle());
            TextView txtContentCardHome = (TextView)relLayout.findViewById(R.id.txtContentCardHome);
            // Set the same width of parent - tollerance
            txtContentCardHome.setWidth(((LinearLayout)txtContentCardHome.getParent()).getWidth()-parentTollerancePX);
            txtContentCardHome.setText(card.getContent());
            Button btnPartecipateRequest = (Button)relLayout.findViewById(R.id.btnPartecipateRequest);
            business.SetButtonRequest(btnPartecipateRequest,true);
            btnPartecipateRequest.setOnClickListener(new View.OnClickListener() {
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

}
