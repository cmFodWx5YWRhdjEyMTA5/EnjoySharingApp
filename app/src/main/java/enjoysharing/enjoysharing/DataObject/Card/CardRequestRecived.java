package enjoysharing.enjoysharing.DataObject.Card;

public class CardRequestRecived extends CardRequestSent {
    public CardRequestRecived(int idEvent, int userId, String username, String title, String content, byte[] userImage, int GenderEventId, int AcceptedRequest, int maxRequest) {
        super(idEvent, userId, username, title, content, userImage, GenderEventId, AcceptedRequest, maxRequest,0);
        this.AcceptedRequest = AcceptedRequest;
        this.MaxRequest = maxRequest;
    }
}
