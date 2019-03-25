package enjoysharing.enjoysharing.Business;

import android.graphics.Bitmap;
import android.util.Log;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import enjoysharing.enjoysharing.DataObject.CurrentUser;
import enjoysharing.enjoysharing.DataObject.JSONServiceResponseOBJ;
import enjoysharing.enjoysharing.DataObject.Parameter;
import enjoysharing.enjoysharing.DataObject.ParameterCollection;

public class BusinessCallService {
    
    protected URL url = null;
    protected HttpURLConnection urlConnection = null;
    protected String serviceURL;
    protected ParameterCollection params;
    protected String servletName;
    protected int timeout = 10000;
    public JSONServiceResponseOBJ retObj;
    protected CurrentUser user;
    protected boolean executePost = false;
    protected boolean executeGet = false;
    // Questa variabile la uso per le esecuzioni a server spento!
    // Di default è true ma in realtà viene valorizzata con quella dell'Activity!
    public boolean simulateCall = true;
    // Siccome la conversione da bitmap a string ci mette molto gliela faccio fare nell'async!
    protected Bitmap bitmap = null;
    protected String bitmapField = "Bitmap";

    public void SetParams(ParameterCollection params) { this.params = params; }
    public void SetLongTimeout() { this.timeout = 20000; }
    public void SetBitmap(String bitmapField, Bitmap bitmap)
    {
        this.bitmapField = bitmapField;
        this.bitmap = bitmap;
    }

    public BusinessCallService(String serviceURL, String servletName,CurrentUser user, boolean executePost,boolean executeGet)
    {
        this.serviceURL = serviceURL;
        this.servletName = servletName;
        this.params = new ParameterCollection();
        this.user = user;
        this.executePost = executePost;
        this.executeGet = executeGet;
        retObj = new JSONServiceResponseOBJ();
    }

    public void AddParameter(String name, Object value)
    {
        params.Add(name, value);
    }

    public void SetParams()
    {
        if(user != null)
        {
            AddParameter("Email",user.getEmail());
            AddParameter("Password",user.getPassword());
        }
    }

    protected String prepareParams()
    {
        String ret="";
        if(bitmap != null)
        {
            String strBitmap = new BusinessBase().ImageToString(bitmap);
            ret += "&"+bitmapField+"="+strBitmap;
        }
        for(Parameter param : params.GetParametersList())
        {
            ret += "&"+param.GetName()+"="+param.GetValue();
        }
        ret = ret.length()>0 ? ret.replaceFirst("&","") : "";
        return ret;
    }

    public boolean Call()
    {
        try
        {
            SetParams();
            if(simulateCall)
                retObj.setStateResponse(true);
            if(executePost && !simulateCall)
                CallPost();
            if(executeGet && !simulateCall)
                CallGet();
        } catch (Exception e)
        {
            retObj.setMessage("GeneralError");
            retObj.setStateResponse(false);
        }
        return true;
    }

    protected void CallGet()
    {
        try
        {
            String paramString = prepareParams();
            url = new URL(serviceURL+servletName+"?"+paramString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(timeout);
            urlConnection.setConnectTimeout(timeout);
            //urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            //urlConnection.setDoOutput(true);
            int responseCode=urlConnection.getResponseCode();
            checkResponse(responseCode);

        } catch (MalformedURLException ex) {
            Log.e("MalformedURL error",ex.getMessage());
            retObj.setMessage("GeneralError");
            retObj.setStateResponse(false);
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Log.e("IOException error",ex.getMessage());
            retObj.setMessage("GeneralError");
            retObj.setStateResponse(false);
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception ex) {
                    Log.e("Close connection error", ex.getMessage());
                    retObj.setMessage("GeneralError");
                    retObj.setStateResponse(false);
                    //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected void CallPost()
    {
        try
        {
            url = new URL(serviceURL+servletName);
            String paramString = prepareParams();
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(timeout);
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(paramString);
            writer.flush();
            writer.close();
            os.close();
            int responseCode=urlConnection.getResponseCode();
            checkResponse(responseCode);
        } catch (Exception e) {
            Log.e("Close connection error",e.getMessage());
            retObj.setMessage("GeneralError");
            retObj.setStateResponse(false);
        } finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception ex) {
                    Log.e("Close connection error", ex.getMessage());
                    retObj.setMessage("GeneralError");
                    retObj.setStateResponse(false);
                    //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected void checkResponse(int responseCode)
    {
        try
        {
            switch (responseCode) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    retObj = new Gson().fromJson(sb.toString(), JSONServiceResponseOBJ.class);
                    break;
                default:
                    Log.e("Service status",""+responseCode);
                    retObj.setMessage("GeneralError");
                    retObj.setStateResponse(false);
                    break;
            }
        }
        catch (Exception e) {
            Log.e("Close connection error",e.getMessage());
            retObj.setMessage("GeneralError");
            retObj.setStateResponse(false);
        }
    }
    
}
