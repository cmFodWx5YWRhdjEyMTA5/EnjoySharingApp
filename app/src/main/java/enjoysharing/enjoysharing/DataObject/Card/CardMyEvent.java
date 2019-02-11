package enjoysharing.enjoysharing.DataObject.Card;

import java.util.Date;

public class CardMyEvent extends CardBase {

    // Simple constructor
    public CardMyEvent(int idEvent, int userId, String username, String title, String content, byte[] userImage, int GenderEventId, int AcceptedRequest, int maxRequest, Date EventDate) {
        super(idEvent, userId, username, title, content, userImage, GenderEventId, EventDate);
        this.AcceptedRequest = AcceptedRequest;
        this.MaxRequest = maxRequest;
    }
}
