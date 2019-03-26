package enjoysharing.enjoysharing.Business;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.util.ArrayList;

import enjoysharing.enjoysharing.Activity.BaseActivity;
import enjoysharing.enjoysharing.DataObject.JSONServiceResponseOBJ;
import enjoysharing.enjoysharing.DataObject.ParameterCollection;
import enjoysharing.enjoysharing.R;

public class BusinessImage extends AsyncTask<Void, Void, Boolean> {

    protected BusinessCallService businessCallService;
    protected ParameterCollection params;
    protected String servletName;
    protected ImageView imageView;
    protected BaseActivity activity;
    protected JSONServiceResponseOBJ retObj;
    protected String userIdImage;
    protected Bitmap bitImage;

    public void AddParameter(String name, Object value)
    {
        params.Add(name, value);
    }

    public BusinessImage(BaseActivity activity)
    {
        this.imageView = null;
        this.activity = activity;
        servletName = "UserServlet";
        params = new ParameterCollection();
        retObj = new JSONServiceResponseOBJ();
        businessCallService = new BusinessCallService(activity.getString(R.string.service_url),servletName,activity.GetUser(),false,true);
        businessCallService.simulateCall = activity.simulateCall;
        AddParameter("RequestType","GI");
        AddParameter("V",activity.getString(R.string.current_version));
    }

    public BusinessImage(ImageView imageView, BaseActivity activity)
    {
        this.imageView = imageView;
        this.activity = activity;
        servletName = "UserServlet";
        params = new ParameterCollection();
        retObj = new JSONServiceResponseOBJ();
        businessCallService = new BusinessCallService(activity.getString(R.string.service_url),servletName,activity.GetUser(),false,true);
        businessCallService.simulateCall = activity.simulateCall;
        AddParameter("RequestType","GI");
        AddParameter("V",activity.getString(R.string.current_version));
    }

    public void SetLongTimeout() {
        if(businessCallService!=null)
            businessCallService.SetLongTimeout();
    }

    @Override
    protected Boolean doInBackground(Void... pars) {
        try
        {
            userIdImage = params.Get("UserIdImage").toString();
            bitImage = null;
            bitImage = activity.LoadProfileImage(userIdImage);
            if(bitImage != null) return true;
            return LoadFromServer();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean LoadFromServer()
    {
        if(businessCallService!=null)
        {
            businessCallService.SetParams(params);
            businessCallService.Call();
            retObj = businessCallService.retObj;
        }
        else
        {
            retObj.setStateResponse(true);
        }
        return retObj.isOkResponse();
    }

    @Override
    protected void onPostExecute(final Boolean result) {
        if(result)
        {
            if(bitImage == null)
            {
                ParameterCollection params = new BusinessJSON().GetParamsByJSON(retObj.getMessage());
                bitImage = new BusinessBase().StringToImage(params.Get("Photo").toString());
                if(bitImage != null)
                    activity.StoreImageProfile(userIdImage,bitImage);
            }
            if(bitImage != null)
            {
                if(imageView != null)
                    activity.InsertImage(imageView, bitImage);
                else
                    LoadUserImages();
            }
        }
    }

    // Used to load users profile images
    protected void LoadUserImages()
    {
        ArrayList<ImageView> imageViews = (ArrayList<ImageView>)activity.userImageToLoad.Get(userIdImage);
        for(ImageView img : imageViews)
        {
            img.setClipToOutline(true);
            activity.InsertImage(img, bitImage);
        }
    }
}