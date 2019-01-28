package enjoysharing.enjoysharing.DataObject;

public class CardHome extends CardBase {

    protected boolean RequestSubmitted;

    public boolean IsRequestSubmitted() { return RequestSubmitted; }

    // Simple constructor
    public CardHome(int idEvent, int userId, String username, String title, String content, byte[] userImage, int GenderEventId, int AcceptedRequest, int maxRequest, boolean RequestSubmitted) {
        super(idEvent, userId, username, title, content, userImage, GenderEventId);
        this.AcceptedRequest = AcceptedRequest;
        this.MaxRequest = maxRequest;
        this.RequestSubmitted = RequestSubmitted;
    }
}
