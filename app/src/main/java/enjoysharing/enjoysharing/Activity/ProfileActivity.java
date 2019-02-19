package enjoysharing.enjoysharing.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import enjoysharing.enjoysharing.AdapterObject.ViewPagerAdapter;
import enjoysharing.enjoysharing.R;

public class ProfileActivity extends BaseActivity {

    protected MenuItem prevMenuItem;
    protected ViewPager profile_form;
    protected int currentMenuPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(ProfileActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        CreateMenuElements();

        mFormView = findViewById(R.id.main_frame);
        mProgressView = findViewById(R.id.progress);

        CreateFragments();
        setupViewPager(profile_form);
        FillUserData();

        // Set default page (HOME)
        profile_form.setCurrentItem(0);
        CallStartFragment(0);
    }
    // Used to create menu elements
    protected void CreateMenuElements()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_profile_menu);
        navigationView.setNavigationItemSelectedListener(menuSelected);

        profile_form = (ViewPager) findViewById(R.id.profile_form);
        // Nav menu Home
        nav_menu_home = (BottomNavigationView) findViewById(R.id.nav_menu);
        nav_menu_home.setOnNavigationItemSelectedListener(tabSelected);

        profile_form.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            // Used when swipe page with touch screen
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    nav_menu_home.getMenu().getItem(0).setChecked(false);

                nav_menu_home.getMenu().getItem(position).setChecked(true);
                prevMenuItem = nav_menu_home.getMenu().getItem(position);
                // Call start fragment when selected
                CallStartFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    // FillUserData
    protected void FillUserData()
    {
        TextView txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtUsername.append(user.getUsername());
        TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtEmail.append(user.getEmail());
    }
    // Used to create fragments
    protected void CreateFragments()
    {
//        homeFragment = new HomeFragment();
//        homeFragment.SetActivity(HomeActivity.this);
//        homeFragment.setCurrentUser(user);
//        homeFragment.setProgressView(mProgressView);
//        requestFragment = new RequestFragment();
//        requestFragment.SetActivity(HomeActivity.this);
//        requestFragment.setCurrentUser(user);
//        myEventsFragment = new MyEventsFragment();
//        myEventsFragment.SetActivity(HomeActivity.this);
//        myEventsFragment.setCurrentUser(user);
//        myEventsFragment.setProgressView(mProgressView);
    }
    // Used to call StartFragment of current fragment
    protected void CallStartFragment(int position)
    {
        currentMenuPosition = position;
        ((ViewPagerAdapter)profile_form.getAdapter()).List().get(position).StartFragment();
    }
    // Used to set view pager for swipe touch screen
    protected void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.AddFragment(homeFragment);
//        adapter.AddFragment(requestFragment);
//        adapter.AddFragment(myEventsFragment);
        viewPager.setAdapter(adapter);
    }// Used when user click in tab menu
    protected BottomNavigationView.OnNavigationItemSelectedListener tabSelected
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_gallery_profile:
                    profile_form.setCurrentItem(0);
                    break;
                case R.id.nav_send_request_profile:
                    profile_form.setCurrentItem(1);
                    break;
                case R.id.nav_my_events_profile:
                    profile_form.setCurrentItem(2);
                    break;
            }
            return true;
        }

    };
    // Used for menu navigation
    protected NavigationView.OnNavigationItemSelectedListener menuSelected
            = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_gallery_profile:
                    break;
                case R.id.nav_send_request_profile:
                    break;
                case R.id.nav_my_events_profile :
                    break;
                default:
                    break;
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

    };

    @Override
    public void onBackPressed() {
        super.StandardOnBackPressed();
    }
}
