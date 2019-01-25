package enjoysharing.enjoysharing.DataObject;

public class CardMyEvent extends CardBase {

    // Simple constructor
    public CardMyEvent(int idCard, int idEvent, int userId, String username, String title, String content, byte[] userImage, int GenderEventId, int AcceptedRequest, int maxRequest) {
        super(idCard, idEvent, userId, username, title, content, userImage, GenderEventId);
        this.AcceptedRequest = AcceptedRequest;
        this.MaxRequest = maxRequest;
    }
}
