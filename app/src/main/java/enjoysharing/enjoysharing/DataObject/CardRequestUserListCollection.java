package enjoysharing.enjoysharing.DataObject;

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
    public void Add(int idEvent, int idCardEvent, String username, String title)
    {
        CardRequestUserList card = GetCard(idEvent);
        if(card != null) card.AddUsername(username);
        else
        {
            card = new CardRequestUserList(idEvent, idCardEvent, username, title);
            cards.add(card);
        }
    }
    public void Add(CardBase card)
    {
        CardRequestUserList cardFound = GetCard(card.getIdEvent());
        if(cardFound != null) cardFound.AddUsername(card.getUsername());
        else
        {
            cardFound = new CardRequestUserList(card.getIdEvent(), card.getIdCard(), card.getUsername(), card.getTitle());
            cards.add(cardFound);
        }
    }
    public CardRequestUserList GetCard(int idEvent)
    {
        for(CardRequestUserList card : cards)
        {
            if(card.getIdEvent() == idEvent)
                return card;
        }
        return null;
    }

}
