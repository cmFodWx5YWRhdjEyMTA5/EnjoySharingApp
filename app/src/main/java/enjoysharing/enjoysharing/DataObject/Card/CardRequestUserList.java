package enjoysharing.enjoysharing.DataObject.Card;


public class CardRequestUserList extends CardBase{

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
        super(EventId, UserId, Username, Title, null, null, 0, null);
        this.UserIdList = UserId + "";
        this.Usernames = Username;
        this.Title = Title;
        UserNumber = 1;
    }

}
