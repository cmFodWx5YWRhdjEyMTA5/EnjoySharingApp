package enjoysharing.enjoysharing.DataObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CurrentUser {

    protected Activity activity;

    protected String username;
    protected String email;
    protected String password;

    public CurrentUser(Activity activity) {
        this.activity = activity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void Clear() {
        username = "";
        email = "";
        password = "";
    }

    // TODO
    // Check for remember me
    public void CheckRememberMe()
    { }
    // Load data from xml
    public void LoadFromXMLFile()
    {
        Context context = activity.getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences("user",0);
        setUsername(sharedPref.getString("username",null));
        setEmail(sharedPref.getString("email",null));
        setPassword(sharedPref.getString("password",null));
    }
    // Store on xml
    public void SaveOnXMLFile()
    {
        Context context = activity.getApplicationContext();
        SharedPreferences e = context.getSharedPreferences("user", 0);
        SharedPreferences.Editor ed=e.edit();
        ed.putString("username", getUsername());
        ed.putString("email", getEmail());
        ed.putString("password", getPassword());
        ed.apply();
    }

}
