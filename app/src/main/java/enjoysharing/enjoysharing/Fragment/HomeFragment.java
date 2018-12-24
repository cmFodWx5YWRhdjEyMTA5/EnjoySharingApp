package enjoysharing.enjoysharing.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import enjoysharing.enjoysharing.Business.BusinessCards;
import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CardHome;
import enjoysharing.enjoysharing.R;

public class HomeFragment extends FragmentBase {

    TableLayout tableHomeCards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        tableHomeCards = (TableLayout) v.findViewById(R.id.tableHomeCards);
        business = new BusinessCards(activity);
        FillHomeCards();
        return v;
    }
    // User to load home cards
    // TODO
    // Fill cards by server call
    protected void FillHomeCards()
    {
        if (mTask != null) {
            homeCards = new CardCollection();
            business.DrawCardsOnTable(homeCards,tableHomeCards);
            return;
        }
        activity.showProgress(true);
        mTask = new FragmentRequestTask();
        mTask.execute((Void) null);
    }

    protected CardCollection homeCards;

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
        business.DrawCardsOnTable(homeCards,tableHomeCards);
    }

}
