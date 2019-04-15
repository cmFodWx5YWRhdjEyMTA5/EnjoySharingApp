package enjoysharing.enjoysharing.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Fragment.FragmentBase;
import enjoysharing.enjoysharing.Fragment.FriendsFragment;
import enjoysharing.enjoysharing.Fragment.NotifierFragment;
import enjoysharing.enjoysharing.Fragment.ProfileFragment;
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
    protected FriendsFragment friendsFragment;
    protected NotifierFragment notifierFragment;
    protected ProfileFragment profileFragment;
    protected Bitmap profilePhoto;
    protected ImageView imgUser;
    protected int REQUEST_CODE = 1;
    //protected MyEventsFragment myEventsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(HomeActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        activityApp = HomeActivity.this;

        business = new BusinessBase(HomeActivity.this);

        CreateMenuElements();
        CreateButtons();

        mFormView = findViewById(R.id.main_frame);
        mProgressView = findViewById(R.id.progress);

        CreateFragments();
        setupViewPager(viewPager);

        // Set default page (HOME)
        viewPager.setCurrentItem(0);
        CallStartFragment(0);

        // Load user data after all to load image
        FillUserData();
    }
    // Used when activity reloaded
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        user.LoadFromXMLFile();
        FillUserData();
        CallStartFragment(currentMenuPosition,true);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //boolean allPermitted = true;
        switch (requestCode) {
            case 101:  // Request for external storage
                FillUserData();
                ((ViewPagerAdapter)viewPager.getAdapter()).List().get(currentMenuPosition).onRequestPermissionsResult(requestCode, permissions, grantResults);
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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

        friendsFragment = new FriendsFragment();
        friendsFragment.SetActivity(HomeActivity.this);
        friendsFragment.setCurrentUser(user);
        friendsFragment.setProgressView(mProgressView);

        notifierFragment = new NotifierFragment();
        notifierFragment.SetActivity(HomeActivity.this);
        notifierFragment.setCurrentUser(user);
        notifierFragment.setProgressView(mProgressView);

        profileFragment= new ProfileFragment();
        profileFragment.SetActivity(HomeActivity.this);
        profileFragment.setCurrentUser(user);
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
        CallStartFragment(position,false);
    }
    // Used to call StartFragment of current fragment
    protected void CallStartFragment(int position, boolean reload)
    {
        currentMenuPosition = position;
        if(reload)
        {
            ((ViewPagerAdapter)viewPager.getAdapter()).List().get(position).ReloadFragment();
        }
        //((ViewPagerAdapter)viewPager.getAdapter()).List().get(position).StartFragment();
    }
    // Used to set view pager for swipe touch screen
    protected void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(homeFragment);
        adapter.AddFragment(requestFragment);
        adapter.AddFragment(friendsFragment);
        adapter.AddFragment(notifierFragment);
        adapter.AddFragment(profileFragment);
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
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.nav_menu_notification:
                    viewPager.setCurrentItem(3);
                    break;
                case R.id.nav_menu_profile:
                    viewPager.setCurrentItem(4);
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
        imgUser = (ImageView) header.findViewById(R.id.imgUser);
        imgUser.setClipToOutline(true);
        business.LoadUserImage(imgUser);
    }
    // When click on card home row -> redirect to detail row!
    @Override
    public void onRowClick(View v)
    { }
}
