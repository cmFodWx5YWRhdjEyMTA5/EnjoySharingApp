package enjoysharing.enjoysharing.DataObject;

import org.json.JSONObject;

public class CardBase {

    protected String username;
    protected String title;
    protected String content;
    protected byte[] userImage;

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

    // Constructor simple
    public CardBase(String username, String title, String content, byte[] userImage) {
        this.username = username;
        this.title = title;
        this.content = content;
        this.userImage = userImage;
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
