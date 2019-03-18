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
    protected String profile_image;

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

    public String getProfileImage() {
        return profile_image;
    }

    public void setProfileImage(String profile_image) {
        this.profile_image = profile_image;
    }

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
        profile_image = "";
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
        String sUserId = sharedPref.getString("userId",null);
        setUserId(sUserId == null?0:Integer.parseInt(sUserId));
        setUsername(sharedPref.getString("username",null));
        setName(sharedPref.getString("name",null));
        setSurname(sharedPref.getString("surname",null));
        setEmail(sharedPref.getString("email",null));
        setPassword(sharedPref.getString("password",null));
        setProfileImage(sharedPref.getString("profile_image",null));
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
        ed.putString("profile_image", getProfileImage());
        ed.apply();
    }

}
