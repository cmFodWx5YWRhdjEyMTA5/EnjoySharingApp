package enjoysharing.enjoysharing.DataObject.Card;

import java.util.ArrayList;
import java.util.List;

public class CardCollection {

    // Logica: Ho la lista originale che viene caricata tramite Add
    //         poi ho la lista filtrata che ritorno col metodo List
    protected List<CardBase> cards;
    protected List<CardBase> cardsFiltered;
    protected boolean filtered;

    // Simple constructor
    public CardCollection()
    {
        cards = new ArrayList<CardBase>();
        filtered = false;
    }

    public List<CardBase> List() { return filtered ? cardsFiltered : cards; }
    public void Clear() { cards.clear(); }
    public void Add(CardBase card) { cards.add(card); }
    public CardBase GetCard(int EventId)
    {
        for(CardBase card : cards)
        {
            if(card.getEventId() == EventId)
                return card;
        }
        return null;
    }

    public void FilterByTitle(String title)
    {
        List<CardBase> resultList = new ArrayList<CardBase>();
        filtered = true;
        cardsFiltered = new ArrayList<CardBase>();
        for(CardBase card : cards)
        {
            if(card.getTitle().contains(title))
                resultList.add(card);
        }
        cardsFiltered = resultList;
    }

    public void FilterByType(String cardType)
    {
        List<CardBase> resultList = new ArrayList<CardBase>();
        filtered = true;
        cardsFiltered = new ArrayList<CardBase>();
        for(CardBase card : cards)
        {
            if(card.getCardType().equals(cardType))
                resultList.add(card);
        }
        cardsFiltered = resultList;
    }

}
