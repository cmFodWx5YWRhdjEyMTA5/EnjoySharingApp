package enjoysharing.enjoysharing.DataObject;


public class CardRequestUserList {

    protected int EventId;
    protected String UserIdList;
    protected String Usernames;
    protected String Title;
    protected int UserNumber = 0, MAX_USERS = 2;

    public int getEventId() { return EventId; }
    public String getUserIdList() { return UserIdList; }
    public String getUsernames() { return Usernames; }
    public String getTitle() { return Title; }

    public void AddUser(int UserId, String Username)
    {
        Usernames = Usernames.replace(" e ",", ");
        if(UserNumber < MAX_USERS) Usernames += " e "+Username;
        else Usernames += " e altri";
        UserIdList += "," + UserId;
        UserNumber++;
    }

    public boolean IsMultiUsers() { return UserNumber > 1; }

    public CardRequestUserList(int EventId, int UserId, String Username, String Title)
    {
        this.EventId = EventId;
        this.UserIdList = UserId + "";
        this.Usernames = Username;
        this.Title = Title;
        UserNumber = 1;
    }

}
