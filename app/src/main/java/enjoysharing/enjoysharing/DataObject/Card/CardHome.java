package enjoysharing.enjoysharing.DataObject.Card;

import java.util.Date;

public class CardHome extends CardBase {

    protected boolean RequestSubmitted;

    public void setRequestSubmitted(boolean RequestSubmitted) { this.RequestSubmitted = RequestSubmitted; }
    public boolean IsRequestSubmitted() { return RequestSubmitted; }

    // Simple constructor
    public CardHome(int idEvent, int userId, String username, String title, String content, byte[] userImage, int GenderEventId, int AcceptedRequest, int maxRequest, boolean RequestSubmitted, Date EventDate) {
        super(idEvent, userId, username, title, content, userImage, GenderEventId, EventDate);
        this.AcceptedRequest = AcceptedRequest;
        this.MaxRequest = maxRequest;
        this.RequestSubmitted = RequestSubmitted;
    }
}
