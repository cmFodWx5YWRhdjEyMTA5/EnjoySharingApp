package enjoysharing.enjoysharing.DataObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CurrentUser {

    protected Activity activity;

    protected int userId;
    protected String username;
    protected String name;
    protected String surname;
    protected String email;
    protected String password;
    protected boolean remember_me;
    protected String lastUpdateDatetimeProfileImage;

    public CurrentUser(Activity activity) {
        this.activity = activity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; RefreshUsername(); }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; RefreshUsername(); }

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

    public String getLastUpdateDatetimeProfileImage() {
        return lastUpdateDatetimeProfileImage;
    }

    public void setLastUpdateDatetimeProfileImage(String lastUpdateDatetimeProfileImage) {
        this.lastUpdateDatetimeProfileImage = lastUpdateDatetimeProfileImage;
    }

    public boolean getRememberMe() { return remember_me; }

    public void setRememberMe(boolean remember_me) { this.remember_me = remember_me; }

    public void RefreshUsername()
    {
        username = surname + " " + name;
    }

    public void Clear() {
        userId = 0;
        username = "";
        email = "";
        password = "";
        name = "";
        surname = "";
        remember_me = false;
        lastUpdateDatetimeProfileImage = "";
    }
    // Load data from xml
    public void LoadFromXMLFile()
    {
        Context context = activity.getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences("user",0);
        String sUserId = sharedPref.getString("userId",null);
        setUserId(sUserId == null?0:Integer.parseInt(sUserId));
        setUsername(sharedPref.getString("username",null));
        setName(sharedPref.getString("name",null));
        setSurname(sharedPref.getString("surname",null));
        setEmail(sharedPref.getString("email",null));
        setPassword(sharedPref.getString("password",null));
        setRememberMe(sharedPref.getString("remember_me","0").equals("1"));
        setLastUpdateDatetimeProfileImage(sharedPref.getString("lastUpdateDatetimeProfileImage",null));
    }
    // Store on xml
    public void SaveOnXMLFile()
    {
        Context context = activity.getApplicationContext();
        SharedPreferences e = context.getSharedPreferences("user", 0);
        SharedPreferences.Editor ed=e.edit();
        ed.putString("userId", String.valueOf(getUserId()));
        ed.putString("username", getUsername());
        ed.putString("name", getName());
        ed.putString("surname", getSurname());
        ed.putString("email", getEmail());
        ed.putString("password", getPassword());
        ed.putString("remember_me", getRememberMe()?"1":"0");
        ed.putString("lastUpdateDatetimeProfileImage", getLastUpdateDatetimeProfileImage());
        ed.apply();
    }

}
