package enjoysharing.enjoysharing.DataObject;

public class CardMyEvent extends CardBase {

    // Simple constructor
    public CardMyEvent(int idEvent, int userId, String username, String title, String content, byte[] userImage, int GenderEventId, int AcceptedRequest, int maxRequest) {
        super(idEvent, userId, username, title, content, userImage, GenderEventId);
        this.AcceptedRequest = AcceptedRequest;
        this.MaxRequest = maxRequest;
    }
}
