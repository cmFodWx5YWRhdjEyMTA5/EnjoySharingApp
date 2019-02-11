package enjoysharing.enjoysharing.DataObject.Card;

import java.util.Date;

public class CardRequestRecived extends CardRequestSent {
    public CardRequestRecived(int idEvent, int userId, String username, String title, String content, byte[] userImage, int GenderEventId, int AcceptedRequest, int maxRequest, Date EventDate) {
        super(idEvent, userId, username, title, content, userImage, GenderEventId, AcceptedRequest, maxRequest,0, EventDate);
        this.AcceptedRequest = AcceptedRequest;
        this.MaxRequest = maxRequest;
    }
}
