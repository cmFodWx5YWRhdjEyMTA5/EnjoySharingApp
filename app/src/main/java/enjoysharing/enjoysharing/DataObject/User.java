package enjoysharing.enjoysharing.DataObject;

public class User {

    protected int UserId;
    protected String UserName;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public void Clear() {
        UserName = "";
    }

    public User(int UserId, String UserName)
    {
        this.UserId = UserId;
        this.UserName = UserName;
    }

}
