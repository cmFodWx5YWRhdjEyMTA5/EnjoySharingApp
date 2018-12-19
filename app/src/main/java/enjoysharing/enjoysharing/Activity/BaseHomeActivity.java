package enjoysharing.enjoysharing.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import enjoysharing.enjoysharing.Business.BusinessCards;
import enjoysharing.enjoysharing.R;

public class BaseHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Context context;
    protected Toolbar toolbar;
    protected ImageButton imgBtnSearch;
    protected DrawerLayout drawer;
    protected NavigationView navigationView;
    protected ImageButton imgBtnAdd;
    protected ImageButton imgBtnRequest;
    protected ImageButton imgBtnEvent;
    protected ImageButton imgBtnNotification;

    protected void SetContext(Context context){ this.context = context; }

    protected void CreateButtons()
    {
        imgBtnSearch = (ImageButton) findViewById(R.id.imgBtnSearch);
        imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeOpenActivity(context, SearchActivity.class);
            }
        });
        imgBtnAdd = (ImageButton) findViewById(R.id.imgBtnAdd);
        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenActivity(context, IUEventActivity.class);
            }
        });
        imgBtnRequest = (ImageButton) findViewById(R.id.imgBtnRequest);
        imgBtnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OpenActivity(context, IUEventActivity.class);
            }
        });
        imgBtnEvent = (ImageButton) findViewById(R.id.imgBtnEvent);
        imgBtnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OpenActivity(context, IUEventActivity.class);
            }
        });
        imgBtnNotification = (ImageButton) findViewById(R.id.imgBtnNotification);
        imgBtnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OpenActivity(context, IUEventActivity.class);
            }
        });
    }

    protected void CreateFormElements()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
        OpenActivity(context, HomeActivity.class);
    }

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