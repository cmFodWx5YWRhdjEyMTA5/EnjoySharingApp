package enjoysharing.enjoysharing.Activity;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import enjoysharing.enjoysharing.Business.BusinessCards;
import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CardHome;
import enjoysharing.enjoysharing.R;

public class HomeActivity extends BaseHomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(HomeActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        business = new BusinessCards(HomeActivity.this);

        CreateMenuElements();
        CreateButtons();
        imgBtnHome.setImageResource(R.drawable.ic_home_selected_custom);
        navigationView.setNavigationItemSelectedListener(this);

        mFormView = findViewById(R.id.home_form);
        mProgressView = findViewById(R.id.home_progress);

        FillUserData();
        FillHomeCards();
    }
    // FillUserData
    protected void FillUserData()
    {
        View header = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) header.findViewById(R.id.txtNavUsername);
        if(navUsername != null) navUsername.setText(user.getUsername());
    }
    // User to load home cards
    // TODO
    // Fill cards by server call
    protected void FillHomeCards()
    {
        if (mTask != null) {
            business.DrawHomeCardsOnTable(new CardCollection());
            return;
        }
        showProgress(true);
        mTask = new RequestTask();
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
        business.DrawHomeCardsOnTable(homeCards);
    }
    // TODO
    // When click on card home row -> redirect to detail row!
    @Override
    public void onRowClick(View v)
    { }
}
