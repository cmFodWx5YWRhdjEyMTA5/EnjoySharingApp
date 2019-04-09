package enjoysharing.enjoysharing.DataObject.Card;

import java.util.Date;

public class CardRequestRecived extends CardBase {

    protected int RequestStatusId;
    protected boolean RequestSubmitted = true;

    public void setRequestSubmitted(boolean RequestSubmitted) { this.RequestSubmitted = RequestSubmitted; }
    public boolean IsRequestSubmitted() { return RequestSubmitted; }

    public int getRequestStatusId() { return RequestStatusId; }

    public CardRequestRecived(int idEvent, int userId, String username, String title, String content, byte[] userImage, int GenderEventId, int AcceptedRequest, int maxRequest, int RequestStatusId, Date EventDate) {
        super(idEvent, userId, username, title, content, userImage, GenderEventId, EventDate);
        this.AcceptedRequest = AcceptedRequest;
        this.MaxRequest = maxRequest;
        this.RequestStatusId = RequestStatusId;
    }
}
