package enjoysharing.enjoysharing.DataObject;

public class JSONServiceResponseOBJ {

    private boolean stateResponse;
    private String returnMessage;

    public boolean isOkResponse() {
        return stateResponse;
    }

    public void setStateResponse(boolean okResponse) {
        this.stateResponse = okResponse;
    }

    public String getMessage() {
        return returnMessage;
    }

    public void setMessage(String message) {
        returnMessage = message;
    }

}