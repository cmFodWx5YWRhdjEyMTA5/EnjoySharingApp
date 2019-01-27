package enjoysharing.enjoysharing.DataObject;

public class CardRequest extends CardBase {

    // Simple constructor
    public CardRequest(int idEvent, int userId, String username, String title, String content, byte[] userImage, int GenderEventId, int AcceptedRequest, int maxRequest) {
        super(idEvent, userId, username, title, content, userImage, GenderEventId);
        this.AcceptedRequest = AcceptedRequest;
        this.MaxRequest = maxRequest;
    }
}
