package enjoysharing.enjoysharing.DataObject.Card;

import java.util.ArrayList;
import java.util.List;

public class CardRequestUserListCollection {

    protected List<CardRequestUserList> cards;

    // Simple constructor
    public CardRequestUserListCollection()
    {
        cards = new ArrayList<CardRequestUserList>();
    }

    public List<CardRequestUserList> List() { return cards; }
    public void Clear() { cards.clear(); }
    public void Add(int idEvent, int userId, String username, String title)
    {
        CardRequestUserList card = GetCard(idEvent);
        if(card != null) card.AddUser(userId, username);
        else
        {
            card = new CardRequestUserList(idEvent, userId, username, title);
            cards.add(card);
        }
    }
    public void Add(CardBase card)
    {
        CardRequestUserList cardFound = GetCard(card.getEventId());
        if(cardFound != null) cardFound.AddUser(card.getUserId(), card.getUserName());
        else
        {
            cardFound = new CardRequestUserList(card.getEventId(), card.getUserId(), card.getUserName(), card.getTitle());
            cards.add(cardFound);
        }
    }
    public CardRequestUserList GetCard(int idEvent)
    {
        for(CardRequestUserList card : cards)
        {
            if(card.getEventId() == idEvent)
                return card;
        }
        return null;
    }

}
