package enjoysharing.enjoysharing.DataObject;


public class CardRequestRecived {

    protected int idEvent;
    protected int idCardEvent;
    protected String usernames;
    protected String title;
    protected int userNumber = 0, MAX_USERS = 2;

    public int getIdEvent() { return idEvent; }
    public int getIdCardEvent() { return idCardEvent; }
    public String getUsernames() { return usernames; }
    public String getTitle() { return title; }

    public void AddUsername(String username)
    {
        usernames = usernames.replace(" e ",", ");
        if(userNumber < MAX_USERS) usernames += " e "+username;
        else usernames += " e altri";
        userNumber++;
    }

    public boolean IsMultiUsers() { return userNumber > 1; }

    public CardRequestRecived(int idEvent, int idCardEvent, String username, String title)
    {
        this.idEvent = idEvent;
        this.idCardEvent = idCardEvent;
        this.usernames = username;
        this.title = title;
        userNumber = 1;
    }

}
