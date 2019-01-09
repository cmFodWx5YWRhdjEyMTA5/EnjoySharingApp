package enjoysharing.enjoysharing.DataObject;

public class CardRequestRecived extends CardRequest {
    public CardRequestRecived(int idCard, int idEvent, String username, String title, String content, byte[] userImage, int genderIndex, int requestNumber, int maxRequest) {
        super(idCard, idEvent, username, title, content, userImage, genderIndex, requestNumber, maxRequest);
    }
}
