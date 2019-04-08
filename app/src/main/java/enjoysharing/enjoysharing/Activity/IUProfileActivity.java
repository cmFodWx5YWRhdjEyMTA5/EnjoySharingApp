package enjoysharing.enjoysharing.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.IOException;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.R;

public class IUProfileActivity extends BaseActivity {

    protected EditText txtName;
    protected EditText txtSurname;
    protected FloatingActionButton btnChooseImage;
    protected Bitmap profilePhoto;
    protected ImageView imgProfile;
    protected int REQUEST_CODE_CHOOSE_PHOTO = 10;
    protected boolean postForImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(IUProfileActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iuprofile);

        business = new BusinessBase(IUProfileActivity.this);

        // Toolbar user for back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        //toolbar.setOverflowIcon(getDrawable(R.drawable.ic_search_custom));
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        mFormView = findViewById(R.id.iuprofile_form);
        mProgressView = findViewById(R.id.iuprofile_progress);

        txtName = (EditText) findViewById(R.id.txtName);
        txtName.setText(user.getName());
        txtSurname = (EditText) findViewById(R.id.txtSurname);
        txtSurname.setText(user.getSurname());

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

        Button btnSaveProfile = (Button) findViewById(R.id.btnSaveProfile);
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveProfile();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        SwipeUpCloseActivityFromResult();
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

    // Used when activity reloaded
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CHOOSE_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null ){
            Uri uri = data.getData();
            try {
                profilePhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                SavePhoto();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    protected void SavePhoto()
    {
        postForImage = true;
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

    // Used to store information profile
    protected void SaveProfile()
    {
        postForImage = false;
        if(CheckInformations())
        {
            showProgress(true);
            mTask = new RequestTask(true, false, "UserServlet");
            mTask.AddParameter("RequestType","UP");
            mTask.AddParameter("Name",txtName.getText());
            mTask.AddParameter("Surname",txtSurname.getText());
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
    }

    private boolean CheckInformations() {
        if (mTask != null) {
            return true;
        }

        // Reset errors.
        txtName.setError(null);
        txtSurname.setError(null);

        // Store values to check
        String name = txtName.getText().toString();
        String surname = txtSurname.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for name
        if (TextUtils.isEmpty(name)) {
            txtName.setError(getString(R.string.error_name_required));
            focusView = txtName;
            cancel = true;
        }
        // Check for surname
        if (TextUtils.isEmpty(surname)) {
            txtSurname.setError(getString(R.string.error_surname_required));
            focusView = txtSurname;
            cancel = true;
        }
        if (cancel) {
            // form field with an error.
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess && retObj.isOkResponse())
        {
            if(postForImage)
            {
                imgProfile.setImageBitmap(profilePhoto);
                SetCurrentUserUpdateDatetime();
                StoreImageProfile(user.getUserId()+"",profilePhoto);
                business.LoadUserImage(imgProfile,true);
            }
            else
            {
                SaveUser();
                onBackPressed();
            }
        }
        else
            ShowShortMessage(retObj.getMessage());
        profilePhoto = null;
        postForImage = false;
    }

    protected void SaveUser()
    {
        user.setName(txtName.getText().toString());
        user.setSurname(txtSurname.getText().toString());
        user.SaveOnXMLFile();
    }
}
