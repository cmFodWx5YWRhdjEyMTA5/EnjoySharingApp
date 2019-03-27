package enjoysharing.enjoysharing.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import enjoysharing.enjoysharing.AdapterObject.ViewPagerAdapter;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Fragment.GalleryFragment;
import enjoysharing.enjoysharing.Fragment.MyEventsFragment;
import enjoysharing.enjoysharing.Fragment.SendRequestFragment;
import enjoysharing.enjoysharing.R;

public class ProfileActivity extends BaseActivity {

    protected MenuItem prevMenuItem;
    protected ViewPager profile_form;
    protected GalleryFragment galleryFragment;
    protected SendRequestFragment sendRequestFragment;
    protected MyEventsFragment myEventsFragment;
    protected LinearLayout image_user_layout;
    protected LinearLayout info_profile_layout;
    protected boolean infoVisible = false;
    protected TextView txtUsername;
    protected Button btnViewInfoProfile;
    protected FloatingActionButton btnChooseImage;
    protected Bitmap profilePhoto;
    protected ImageView imgProfile;
    protected int REQUEST_CODE = 1;
    protected boolean swipeState; // true = down done, false = up done

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(ProfileActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        business = new BusinessBase(ProfileActivity.this);

        CreateMenuElements();

        mFormView = findViewById(R.id.main_frame);
        mProgressView = findViewById(R.id.progress);

        Button btnModifyProfile = (Button) findViewById(R.id.btnModifyProfile);
        btnModifyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeDownOpenActivity(ProfileActivity.this, IUProfileActivity.class);
            }
        });

        image_user_layout = (LinearLayout) findViewById(R.id.image_user_layout);

        info_profile_layout = findViewById(R.id.info_profile_layout);
        btnViewInfoProfile = findViewById(R.id.btnViewInfoProfile);
        btnViewInfoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(infoVisible)
                {
                    collapseChilds(info_profile_layout);
                    ((Button)view).setText(getString(R.string.btnShowViewInfoProfile));
                }
                else
                {
                    expandChilds(info_profile_layout);
                    ((Button)view).setText(getString(R.string.btnHideViewInfoProfile));
                }
                infoVisible = !infoVisible;
            }
        });

        CreateFragments();
        setupViewPager(profile_form);
        FillUserData();
        swipeState = true;

        // Set this to use onTouch events!
        mFormView.setOnTouchListener(this);
        // Set default page (HOME)
        profile_form.setCurrentItem(0);
        CallStartFragment(0);
    }
    // Quando effettuo lo swipe up devo nascondere immagine profilo e nome utente
    @Override
    protected void ActivityUpSwipe(float deltaY)
    {
        if(swipeState)
        {
            if(infoVisible)
                btnViewInfoProfile.performClick();
            collapse(btnViewInfoProfile);
            collapse(txtUsername);
            collapseImageView(btnChooseImage,500,0);
            collapseImageView(imgProfile,1000, business.PixelToDP(50));
            swipeState = !swipeState;
        }
    }
    // Quando effettuo lo swipe down devo mostrare immagine profilo e nome utente
    @Override
    protected void ActivityDownSwipe(float deltaY)
    {
        if(!swipeState)
        {
            expandImageView(imgProfile,1000,business.PixelToDP(100));
            expandImageView(btnChooseImage,1000,business.PixelToDP(40));
            expand(txtUsername, 1300);
            expand(btnViewInfoProfile,1300,business.PixelToDP(25));
            swipeState = !swipeState;
        }
    }
    // Used when activity reloaded
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null ){
            Uri uri = data.getData();
            try {
                profilePhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                SavePhoto();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else
        {
            user.LoadFromXMLFile();
            FillUserData();
        }
        CallStartFragment(currentMenuPosition);
    }
    // Used to save new photo profile
    protected void SavePhoto()
    {
        mTask = new RequestTask(true, false, "UserServlet",false);
        mTask.AddParameter("RequestType","SP");  // Save Photo
        mTask.SetBitmap("Photo",profilePhoto);
        try
        {
            mTask.execute();
        }
        catch (Exception e)
        {
            retObj.setStateResponse(false);
            retObj.setMessage("GeneralError");
        }
    }
    // Used to create menu elements
    protected void CreateMenuElements()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        /*drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        profile_form = (ViewPager) findViewById(R.id.profile_form);
        // Nav menu Home
        nav_menu_home = (BottomNavigationView) findViewById(R.id.nav_profile_menu);
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
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtUsername.setText(user.getUsername());

        TextView txtName = (TextView) findViewById(R.id.txtName);
        txtName.setText(user.getName());

        TextView txtSurname = (TextView) findViewById(R.id.txtSurname);
        txtSurname.setText(user.getSurname());

        TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtEmail.setText(user.getEmail());

        imgProfile  = (ImageView) findViewById(R.id.imgProfile);
        imgProfile.setClipToOutline(true);
        business.LoadUserImage(imgProfile);

        btnChooseImage = (FloatingActionButton) findViewById(R.id.btnChooseImage);
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        profilePhoto = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    protected void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_CODE);
        }
    }
    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess && retObj.isOkResponse())
        {
            imgProfile.setImageBitmap(profilePhoto);
            user.setProfileImage(business.ImageToString(profilePhoto));
            user.SaveOnXMLFile();
        }
        else
            ShowShortMessage(retObj.getMessage());
        profilePhoto = null;
    }
    // Used to create fragments
    protected void CreateFragments()
    {
        View mProgressViewTab = findViewById(R.id.progress_tab);
        galleryFragment = new GalleryFragment();
        galleryFragment.SetActivity(ProfileActivity.this);
        galleryFragment.setCurrentUser(user);
        galleryFragment.setFormView(profile_form);
        galleryFragment.setProgressView(mProgressViewTab);
        sendRequestFragment = new SendRequestFragment();
        sendRequestFragment.SetActivity(ProfileActivity.this);
        sendRequestFragment.setCurrentUser(user);
        sendRequestFragment.setFormView(profile_form);
        sendRequestFragment.setProgressView(mProgressViewTab);
        myEventsFragment = new MyEventsFragment();
        myEventsFragment.SetActivity(ProfileActivity.this);
        myEventsFragment.setCurrentUser(user);
        myEventsFragment.setFormView(profile_form);
        myEventsFragment.setProgressView(mProgressViewTab);
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
        adapter.AddFragment(galleryFragment);
        adapter.AddFragment(sendRequestFragment);
        adapter.AddFragment(myEventsFragment);
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
}
