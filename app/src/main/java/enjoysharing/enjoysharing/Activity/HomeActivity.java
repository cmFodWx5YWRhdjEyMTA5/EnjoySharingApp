package enjoysharing.enjoysharing.Activity;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.TextView;
import enjoysharing.enjoysharing.Fragment.RecivedRequestFragment;
import enjoysharing.enjoysharing.AdapterObject.ViewPagerAdapter;
import enjoysharing.enjoysharing.R;
import enjoysharing.enjoysharing.Fragment.HomeFragment;

public class HomeActivity extends BaseActivity {

    protected MenuItem prevMenuItem;
    protected ImageButton imgBtnSearch;
    protected ImageButton imgBtnAddEvent;
    protected ViewPager viewPager;
    protected HomeFragment homeFragment;
    protected RecivedRequestFragment requestFragment;
    //protected MyEventsFragment myEventsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(HomeActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CreateMenuElements();
        CreateButtons();

        mFormView = findViewById(R.id.main_frame);
        mProgressView = findViewById(R.id.progress);

        CreateFragments();
        setupViewPager(viewPager);

        FillUserData();
        // Set default page (HOME)
        viewPager.setCurrentItem(0);
        CallStartFragment(0);
    }
    // Used when activity reloaded
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CallStartFragment(currentMenuPosition);
    }
    // Used to create fragments
    protected void CreateFragments()
    {
        homeFragment = new HomeFragment();
        homeFragment.SetActivity(HomeActivity.this);
        homeFragment.setCurrentUser(user);
        homeFragment.setProgressView(mProgressView);
        requestFragment = new RecivedRequestFragment();
        requestFragment.SetActivity(HomeActivity.this);
        requestFragment.setCurrentUser(user);
        /*myEventsFragment = new MyEventsFragment();
        myEventsFragment.SetActivity(HomeActivity.this);
        myEventsFragment.setCurrentUser(user);
        myEventsFragment.setProgressView(mProgressView);*/
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuSelected);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // Nav menu Home
        nav_menu_home = (BottomNavigationView) findViewById(R.id.nav_menu);
        nav_menu_home.setOnNavigationItemSelectedListener(tabSelected);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    // Used to call StartFragment of current fragment
    protected void CallStartFragment(int position)
    {
        currentMenuPosition = position;
        ((ViewPagerAdapter)viewPager.getAdapter()).List().get(position).StartFragment();
    }
    // Used to set view pager for swipe touch screen
    protected void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(homeFragment);
        adapter.AddFragment(requestFragment);
        //adapter.AddFragment(myEventsFragment);
        viewPager.setAdapter(adapter);
    }
    // Used when user click in tab menu
    protected BottomNavigationView.OnNavigationItemSelectedListener tabSelected
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_menu_home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.nav_menu_requests:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.nav_menu_friends:
                    // TODO
                    break;
                case R.id.nav_menu_notification:
                    // TODO
                    break;
            }
            return true;
        }

    };
    // Used to create bottons on top right
    protected void CreateButtons()
    {
        imgBtnSearch = (ImageButton) findViewById(R.id.imgBtnSearch);
                imgBtnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                SwipeOpenActivity(context, SearchActivity.class);
            }
        });
        imgBtnAddEvent = (ImageButton) findViewById(R.id.imgBtnAddEvent);
        imgBtnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeOpenActivityNoFinish(context, IUEventActivity.class);
            }
        });
    }
    // Used for menu navigation
    protected NavigationView.OnNavigationItemSelectedListener menuSelected
            = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_my_account:
                    OpenActivityNoFinish(context,ProfileActivity.class);
                    break;
                case R.id.nav_settings:
                    OpenActivityNoFinish(context, SettingsActivity.class);
                    break;
                case R.id.nav_logout :
                    user.Clear();
                    user.SaveOnXMLFile();
                    SwipeCloseActivity(context, LoginActivity.class);
                    break;
                default:
                    break;
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

    };
    // FillUserData
    protected void FillUserData()
    {
        View header = navigationView.getHeaderView(0);
        TextView txtNavUsername = (TextView) header.findViewById(R.id.txtNavUsername);
        txtNavUsername.setText(user.getUsername());
    }
    // When click on card home row -> redirect to detail row!
    @Override
    public void onRowClick(View v)
    { }
}
