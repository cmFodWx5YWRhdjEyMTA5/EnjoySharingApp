package enjoysharing.enjoysharing.DataObject;

public class CardHome extends CardBase {

    // Simple constructor
    public CardHome(String username, String title, String content, byte[] userImage, int genderIndex, int requestNumber, int maxRequest) {
        super(username, title, content, userImage, genderIndex);
        this.requestNumber = requestNumber;
        this.maxRequest = maxRequest;
    }
}
