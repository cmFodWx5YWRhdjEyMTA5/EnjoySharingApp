package enjoysharing.enjoysharing.DataObject;

public class User {

    protected int idUser;
    protected String username;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void Clear() {
        username = "";
    }

    public User(int idUser, String username)
    {
        this.idUser = idUser;
        this.username = username;
    }

}
