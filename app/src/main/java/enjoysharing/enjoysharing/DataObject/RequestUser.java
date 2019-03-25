package enjoysharing.enjoysharing.DataObject;

import java.util.Date;

public class RequestUser extends User {

    protected int Status; // 0 = undefined, 1 = accepted, 2 = declined
    protected Date LastUpdateProfileImage;

    public void SetStatus(int status) { this.Status = status; }
    public boolean isUndefined() { return Status == 0; }
    public boolean isAccepted() { return Status == 1; }
    public boolean isDeclined() { return Status == 2; }

    public void setLastUpdateProfileImage(Date LastUpdateProfileImage) { this.LastUpdateProfileImage = LastUpdateProfileImage; }

    public Date getLastUpdateProfileImage() { return LastUpdateProfileImage; }

    public RequestUser(int UserId, String UserName, int Status) {
        super(UserId, UserName);
        this.Status = Status;
    }
}