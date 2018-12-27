package enjoysharing.enjoysharing.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CardHome;
import enjoysharing.enjoysharing.R;

public class SearchActivity extends BaseActivity {

    // TODO
    // To delete when call server
    protected String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(SearchActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        business = new BusinessBase(SearchActivity.this);
        // Toolbar user for back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Search);
        //toolbar.setOverflowIcon(getDrawable(R.drawable.ic_search_custom));
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        EditText searchTo = (EditText)findViewById(R.id.txtSearch);
        searchTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchCards(s.toString());
            }
        });

        mFormView = findViewById(R.id.search_form);
        mProgressView = findViewById(R.id.search_progress);
    }

    // Search cards based on title
    protected void SearchCards(String title)
    {
        if (mTask != null) {
            mTask.cancel(true);
        }
        // TODO
        // To delete when call server
        this.title = title;
        showProgress(true);
        mTask = new RequestTask();
        mTask.execute((Void) null);
    }

    protected CardCollection searchCards;

    @Override
    protected void DoInBackground()
    {
        searchCards = new CardCollection();
        searchCards.Add(new CardHome("Utente 1","Titolo 1","Contenuto di prova 1 caricato da codice",null));
        searchCards.Add(new CardHome("Utente 2","Titolo 2","Contenuto di prova 2 caricato da codice",null));
        searchCards.Add(new CardHome("Utente 3","Titolo 3","Contenuto di prova 3 caricato da codice",null));
        searchCards.Add(new CardHome("Utente 4","Titolo 4","Contenuto di prova 4 caricato da codice",null));
        searchCards.FilterByTitle(title);
    }

    @Override
    protected void OnRequestPostExecute()
    {
        TableLayout searchTable = (TableLayout) findViewById(R.id.tblSearchCards);
        // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
        DrawCardsOnTable(searchCards, searchTable);
    }

    protected void DrawCardsOnTable(CardCollection cards, TableLayout table)
    {
        table.removeAllViews();
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(80);
        int parentTollerancePX = 5;
        for (int i=0; i<cards.List().size(); i++) {
            CardHome card = (CardHome)cards.List().get(i);
            TableRow row = (TableRow) LayoutInflater.from(SearchActivity.this).inflate(R.layout.card_home, null);
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
            business.SetButonRequest(btnPartecipateRequest,true);
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
