package enjoysharing.enjoysharing.Fragment;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import enjoysharing.enjoysharing.Activity.IUProfileActivity;
import enjoysharing.enjoysharing.AdapterObject.ViewPagerAdapter;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.R;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends FragmentBase {

    protected BottomNavigationView nav_profile_menu;
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
    protected int REQUEST_CODE_CHOOSE_PHOTO = 10;
    protected boolean swipeState; // true = down done, false = up done

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_profile, container, false);
        setFormView(vMain.findViewById(R.id.main_frame));
        setProgressView(vMain.findViewById(R.id.progress));
        InitFragment();
        super.onCreateView(inflater,container,savedInstanceState);
        return vMain;
    }
    protected void InitFragment()
    {
        CreateMenuElements();

        CreateFragments();
        setupViewPager(profile_form);
        swipeState = true;

        Button btnModifyProfile = (Button) vMain.findViewById(R.id.btnModifyProfile);
        btnModifyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeDownOpenActivity(activity, IUProfileActivity.class);
            }
        });

        image_user_layout = (LinearLayout) vMain.findViewById(R.id.image_user_layout);

        info_profile_layout = vMain.findViewById(R.id.info_profile_layout);
        btnViewInfoProfile = vMain.findViewById(R.id.btnViewInfoProfile);
        btnViewInfoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(infoVisible)
                {
                    activity.collapseChilds(info_profile_layout);
                    ((Button)view).setText(getString(R.string.btnShowViewInfoProfile));
                }
                else
                {
                    activity.expandChilds(info_profile_layout);
                    ((Button)view).setText(getString(R.string.btnHideViewInfoProfile));
                }
                infoVisible = !infoVisible;
            }
        });
    }
    @Override
    protected boolean CheckForCurrentFragment() { return activity.getCurrentMenuPosition()==4; }
    @Override
    protected void ShowProgress(boolean state)
    {
        ShowProgressPassView(state);
    }
    @Override
    public void StartFragment()
    {
        business = new BusinessBase(activity);
        super.StartFragment();
        FillUserData();
        CallStartFragment(currentMenuPosition);
    }
    // Used when activity reloaded
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CHOOSE_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null ){
            Uri uri = data.getData();
            try {
                profilePhoto = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
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
    protected void SavePhoto()
    {
        mTask = new FragmentRequestTask(true, false, "UserServlet",false);
        mTask.AddParameter("RequestType","SP");  // Save Photo
        mTask.SetBitmap("Photo",profilePhoto);
        try
        {
            mTask.execute();
        }
        catch (Exception e)
        {
            activity.retObj.setStateResponse(false);
            activity.retObj.setMessage("GeneralError");
        }
    }
    // Used to create menu elements
    protected void CreateMenuElements()
    {
        profile_form = (ViewPager) vMain.findViewById(R.id.profile_form);
        // Nav menu Home
        nav_profile_menu = (BottomNavigationView) vMain.findViewById(R.id.nav_profile_menu);
        nav_profile_menu.setOnNavigationItemSelectedListener(tabSelected);

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
                    nav_profile_menu.getMenu().getItem(0).setChecked(false);

                nav_profile_menu.getMenu().getItem(position).setChecked(true);
                prevMenuItem = nav_profile_menu.getMenu().getItem(position);
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
        txtUsername = (TextView) vMain.findViewById(R.id.txtUsername);
        txtUsername.setText(user.getUsername());

        TextView txtName = (TextView) vMain.findViewById(R.id.txtName);
        txtName.setText(user.getName());

        TextView txtSurname = (TextView) vMain.findViewById(R.id.txtSurname);
        txtSurname.setText(user.getSurname());

        TextView txtEmail = (TextView) vMain.findViewById(R.id.txtEmail);
        txtEmail.setText(user.getEmail());

        imgProfile  = (ImageView) vMain.findViewById(R.id.imgProfile);
        imgProfile.setClipToOutline(true);
        business.LoadUserImage(imgProfile);

        btnChooseImage = (FloatingActionButton) vMain.findViewById(R.id.btnChooseImage);
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
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_CODE_CHOOSE_PHOTO);
        }
    }
    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess && activity.retObj.isOkResponse())
        {
            imgProfile.setImageBitmap(profilePhoto);
            user.setProfileImage(business.ImageToString(profilePhoto));
            user.SaveOnXMLFile();
        }
        else
            ShowShortMessage(activity.retObj.getMessage());
        profilePhoto = null;
    }
    // Used to create fragments
    protected void CreateFragments()
    {
        View mProgressViewTab = vMain.findViewById(R.id.progress_tab);
        galleryFragment = new GalleryFragment();
        galleryFragment.SetActivity(activity);
        galleryFragment.setCurrentUser(user);
        galleryFragment.setFormView(profile_form);
        galleryFragment.setProgressView(mProgressViewTab);
        galleryFragment.setParentFragment(this);
        sendRequestFragment = new SendRequestFragment();
        sendRequestFragment.SetActivity(activity);
        sendRequestFragment.setCurrentUser(user);
        sendRequestFragment.setFormView(profile_form);
        sendRequestFragment.setProgressView(mProgressViewTab);
        sendRequestFragment.setParentFragment(this);
        myEventsFragment = new MyEventsFragment();
        myEventsFragment.SetActivity(activity);
        myEventsFragment.setCurrentUser(user);
        myEventsFragment.setFormView(profile_form);
        myEventsFragment.setProgressView(mProgressViewTab);
        myEventsFragment.setParentFragment(this);
    }
    // Used to call StartFragment of current fragment
    protected void CallStartFragment(int position)
    {
        currentMenuPosition = position;
        FragmentBase fragment = ((ViewPagerAdapter)profile_form.getAdapter()).List().get(position);
        getChildFragmentManager()
                .beginTransaction()
                .detach(fragment)
                .attach(fragment)
                .commit();
        fragment.StartFragment();
    }
    // Used to set view pager for swipe touch screen
    protected void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.AddFragment(galleryFragment);
        adapter.AddFragment(sendRequestFragment);
        adapter.AddFragment(myEventsFragment);
        viewPager.setAdapter(adapter);
    }
    // Used when user click in tab menu
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
