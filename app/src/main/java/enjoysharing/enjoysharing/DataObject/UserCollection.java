package enjoysharing.enjoysharing.DataObject;

import java.util.ArrayList;
import java.util.List;

public class UserCollection {

    protected List<User> users;
    protected List<User> usersFiltered;
    protected boolean filtered;
    protected int numPerson, maxRequest;

        // Simple constructor
    public UserCollection()
    {
        users = new ArrayList<User>();
        filtered = false;
    }

    public List<User> List() { return filtered ? usersFiltered : users; }
    public void Clear() { users.clear(); }
    public void Add(User user) { users.add(user); }
    public User GetUser(int idUser)
    {
        for(User user : users)
        {
            if(user.getIdUser() == idUser)
                return user;
        }
        return null;
    }

    public void FilterByUsername(String username)
    {
        List<User> resultList = new ArrayList<User>();
        if(!filtered)
        {
            filtered = true;
            usersFiltered = new ArrayList<User>();
            for(User card : users)
            {
                if(card.getUsername().contains(username))
                    resultList.add(card);
            }
        }
        else
        {
            for(User user : usersFiltered)
            {
                if(user.getUsername().contains(username))
                    resultList.add(user);
            }
        }
        usersFiltered = resultList;
    }

}
