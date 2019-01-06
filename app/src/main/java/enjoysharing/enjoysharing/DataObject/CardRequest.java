package enjoysharing.enjoysharing.DataObject;

public class CardRequest extends CardBase {

    // Simple constructor
    public CardRequest(int idCard, int idEvent, String username, String title, String content, byte[] userImage, int genderIndex, int requestNumber, int maxRequest) {
        super(idCard, idEvent, username, title, content, userImage, genderIndex);
        this.requestNumber = requestNumber;
        this.maxRequest = maxRequest;
    }
}
