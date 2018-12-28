package enjoysharing.enjoysharing.DataObject;

public class CardMyEvent extends CardBase {

    // Simple constructor
    public CardMyEvent(String username, String title, String content, byte[] userImage, int requestNumber, int maxRequest) {
        super(username, title, content, userImage);
        this.requestNumber = requestNumber;
        this.maxRequest = maxRequest;
    }
}
