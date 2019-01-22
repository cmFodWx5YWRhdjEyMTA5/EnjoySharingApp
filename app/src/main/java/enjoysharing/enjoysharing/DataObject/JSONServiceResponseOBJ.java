package enjoysharing.enjoysharing.DataObject;

public class JSONServiceResponseOBJ {

    private boolean stateResponse;
    private String message;

    public boolean isOkResponse() {
        return stateResponse;
    }

    public void setStateResponse(boolean okResponse) {
        this.stateResponse = okResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        message = message;
    }

}