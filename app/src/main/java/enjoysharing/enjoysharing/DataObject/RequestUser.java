package enjoysharing.enjoysharing.DataObject;

public class RequestUser extends User {

    protected int Status; // 0 = undefined, 1 = accepted, 2 = declined

    public void SetStatus(int status) { this.Status = status; }
    public boolean isUndefined() { return Status == 0; }
    public boolean isAccepted() { return Status == 1; }
    public boolean isDeclined() { return Status == 2; }

    public RequestUser(int UserId, String UserName, int Status) {
        super(UserId, UserName);
        this.Status = Status;
    }
}