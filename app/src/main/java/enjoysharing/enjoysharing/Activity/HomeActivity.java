package enjoysharing.enjoysharing.Activity;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        business = new BusinessCards(HomeActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        //toolbar.setOverflowIcon(getDrawable(R.drawable.ic_search_custom));
        setSupportActionBar(toolbar);

        ImageButton imgBtnSearchHome = (ImageButton) findViewById(R.id.imgBtnSearchHome);
        imgBtnSearchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeOpenActivity(HomeActivity.this, SearchActivity.class);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFormView = findViewById(R.id.home_form);
        mProgressView = findViewById(R.id.home_progress);

        FillUserData(navigationView);
        FillHomeCards();
    }
    // FillUserData
    protected void FillUserData(NavigationView navigationView)
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
        homeCards.Add(new CardHome("Utente 3","Titolo 3","Contenuto di prova 3 caricato da codice",null));
        homeCards.Add(new CardHome("Utente 4","Titolo 4","Contenuto di prova 4 caricato da codice",null));
    }

    @Override
    protected void OnRequestPostExecute()
    {
        // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
        business.DrawHomeCardsOnTable(homeCards);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
