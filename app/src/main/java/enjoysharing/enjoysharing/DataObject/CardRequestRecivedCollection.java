package enjoysharing.enjoysharing.DataObject;

import java.util.ArrayList;
import java.util.List;

public class CardRequestRecivedCollection {

    protected List<CardRequestRecived> cards;

    // Simple constructor
    public CardRequestRecivedCollection()
    {
        cards = new ArrayList<CardRequestRecived>();
    }

    public List<CardRequestRecived> List() { return cards; }
    public void Clear() { cards.clear(); }
    public void Add(int idEvent, int idCardEvent, String username, String title)
    {
        CardRequestRecived card = GetCard(idEvent);
        if(card != null) card.AddUsername(username);
        else
        {
            card = new CardRequestRecived(idEvent, idCardEvent, username, title);
            cards.add(card);
        }
    }
    public void Add(CardBase card)
    {
        CardRequestRecived cardFound = GetCard(card.getIdEvent());
        if(cardFound != null) cardFound.AddUsername(card.getUsername());
        else
        {
            cardFound = new CardRequestRecived(card.getIdEvent(), card.getIdCard(), card.getUsername(), card.getTitle());
            cards.add(cardFound);
        }
    }
    public CardRequestRecived GetCard(int idEvent)
    {
        for(CardRequestRecived card : cards)
        {
            if(card.getIdEvent() == idEvent)
                return card;
        }
        return null;
    }

}
