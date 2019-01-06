package enjoysharing.enjoysharing.DataObject;

public class RequestUser extends User {

    protected int status; // 0 = undefined, 1 = accepted, 2 = declined

    public void SetStatus(int status) { this.status = status; }
    public boolean isUndefined() { return status == 0; }
    public boolean isAccepted() { return status == 1; }
    public boolean isDeclined() { return status == 2; }

    public RequestUser(int idUser, String username, int status) {
        super(idUser, username);
        this.status = status;
    }
}