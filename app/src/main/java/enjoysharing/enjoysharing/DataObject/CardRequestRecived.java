package enjoysharing.enjoysharing.DataObject;

public class CardRequestRecived extends CardRequest {
    public CardRequestRecived(int idEvent, int userId, String username, String title, String content, byte[] userImage, int GenderEventId, int AcceptedRequest, int maxRequest) {
        super(idEvent, userId, username, title, content, userImage, GenderEventId, AcceptedRequest, maxRequest);
    }
}
