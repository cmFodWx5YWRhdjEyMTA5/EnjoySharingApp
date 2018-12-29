package enjoysharing.enjoysharing.DataObject;

import org.json.JSONObject;
import java.io.Serializable;

public class CardBase implements Serializable {

    protected String username;
    protected String title;
    protected String content;
    protected byte[] userImage;
    protected int genderIndex;
    protected int requestNumber;
    protected int maxRequest;

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(int requestNumber) {
        this.requestNumber = requestNumber;
    }

    public int getMaxRequest() {
        return maxRequest;
    }

    public void setMaxRequest(int maxRequest) {
        this.maxRequest = maxRequest;
    }

    public int getGenderIndex() {
        return genderIndex;
    }

    // Constructor simple
    public CardBase(String username, String title, String content, byte[] userImage, int genderIndex) {
        this.username = username;
        this.title = title;
        this.content = content;
        this.userImage = userImage;
        this.genderIndex = genderIndex;
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
