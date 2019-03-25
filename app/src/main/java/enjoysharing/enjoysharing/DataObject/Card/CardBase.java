package enjoysharing.enjoysharing.DataObject.Card;

import org.json.JSONObject;
import java.io.Serializable;
import java.util.Date;

public class CardBase implements Serializable {

    protected String CardType;
    protected int EventId;
    protected int UserId;
    protected String UserName;
    protected String Title;
    protected String Content;
    protected byte[] UserImage;
    protected int GenderEventId;
    protected int AcceptedRequest;
    protected int MaxRequest;
    protected Date DateEvent;
    protected Date LastUpdateProfileImage;

    public String getCardType() {
        return CardType;
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

    public void setDateEvent(Date dateEvent) {
        DateEvent = dateEvent;
    }

    public Date getDateEvent() {
        return DateEvent;
    }

    public void setLastUpdateProfileImage(Date LastUpdateProfileImage) { this.LastUpdateProfileImage = LastUpdateProfileImage; }

    public Date getLastUpdateProfileImage() { return LastUpdateProfileImage; }

    // Constructor simple
    public CardBase(int EventId, int UserId, String UserName, String Title, String Content, byte[] UserImage, int GenderEventId, Date DateEvent) {
        this.EventId = EventId;
        this.UserId = UserId;
        this.UserName = UserName;
        this.Title = Title;
        this.Content = Content;
        this.UserImage = UserImage;
        this.GenderEventId = GenderEventId;
        this.DateEvent = DateEvent;
    }

}
