package enjoysharing.enjoysharing.Activity;

import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import enjoysharing.enjoysharing.Business.BusinessCards;
import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CardHome;
import enjoysharing.enjoysharing.R;

public class IUEventActivity extends BaseHomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(IUEventActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iuevent);

        business = new BusinessCards(IUEventActivity.this);

        CreateFormElements();
        CreateButtons();
        imgBtnAdd.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.button_icon_home_selected_first) );
        navigationView.setNavigationItemSelectedListener(this);

        mFormView = findViewById(R.id.iuevent_form);
        mProgressView = findViewById(R.id.iuevent_progress);
    }
    // TODO
    // Do request call ONLY FOR UPDATE
    @Override
    protected void DoInBackground()
    { }
    // TODO
    // Used to fill fields ONLY FOR UPDATE
    @Override
    protected void OnRequestPostExecute()
    { }
}
