package enjoysharing.enjoysharing.DataObject;

import org.json.JSONObject;
import java.io.Serializable;

public class CardBase implements Serializable {

    protected String CardType;
    protected int CardId;
    protected int EventId;
    protected int UserId;
    protected String UserName;
    protected String Title;
    protected String Content;
    protected byte[] UserImage;
    protected int GenderEventId;
    protected int AcceptedRequest;
    protected int MaxRequest;

    public String getCardType() {
        return CardType;
    }

    public int getCardId() {
        return CardId;
    }

    public int getEventId() {
        return EventId;
    }

    public int getUserId() {
        return UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public String getTitle() {
        return Title;
    }

    public String getContent() {
        return Content;
    }

    public byte[] getUserImage() {
        return UserImage;
    }

    public int getAcceptedRequest() {
        return AcceptedRequest;
    }

    public void setAcceptedRequest(int AcceptedRequest) {
        this.AcceptedRequest = AcceptedRequest;
    }

    public int getMaxRequest() {
        return MaxRequest;
    }

    public void setMaxRequest(int MaxRequest) {
        this.MaxRequest = MaxRequest;
    }

    public int getGenderEventId() {
        return GenderEventId;
    }

    // Constructor simple
    public CardBase(int CardId, int EventId, int UserId, String UserName, String Title, String Content, byte[] UserImage, int GenderEventId) {
        this.CardId = CardId;
        this.EventId = EventId;
        this.UserId = UserId;
        this.UserName = UserName;
        this.Title = Title;
        this.Content = Content;
        this.UserImage = UserImage;
        this.GenderEventId = GenderEventId;
    }

    // TODO
    // Load card from json object
    protected void LoadFromJSON(JSONObject json)
    { }

    // TODO
    // Load card from user
    protected void LoadFromUser(Object user)
    { }



}
