package enjoysharing.enjoysharing.DataObject;

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
        if(!filtered)
        {
            filtered = true;
            cardsFiltered = new ArrayList<CardBase>();
            for(CardBase card : cards)
            {
                if(card.getTitle().contains(title))
                    resultList.add(card);
            }
        }
        else
        {
            for(CardBase card : cardsFiltered)
            {
                if(card.getTitle().contains(title))
                    resultList.add(card);
            }
        }
        cardsFiltered = resultList;
    }

    public void FilterByNumberPerson(String personNumber)
    {
        int iPersonNumber =  -1;
        if(!(personNumber == null || personNumber.equals("")))
            iPersonNumber = Integer.parseInt(personNumber);
        List<CardBase> resultList = new ArrayList<CardBase>();
        if(!filtered)
        {
            filtered = true;
            cardsFiltered = new ArrayList<CardBase>();
            for(CardBase card : cards)
            {
                if(iPersonNumber == -1 || card.getAcceptedRequest() >= iPersonNumber)
                    resultList.add(card);
            }
        }
        else
        {
            for(CardBase card : cardsFiltered)
            {
                if(iPersonNumber == -1 || card.getAcceptedRequest() >= iPersonNumber)
                    resultList.add(card);
            }
        }
        cardsFiltered = resultList;
    }

    public void FilterByGender(int genderIndex)
    {
        List<CardBase> resultList = new ArrayList<CardBase>();
        if(!filtered)
        {
            filtered = true;
            cardsFiltered = new ArrayList<CardBase>();
            for(CardBase card : cards)
            {
                if(genderIndex == -1 || card.getGenderEventId() == genderIndex)
                    resultList.add(card);
            }
        }
        else
        {
            for(CardBase card : cardsFiltered)
            {
                if(genderIndex == -1 || card.getGenderEventId() == genderIndex)
                    resultList.add(card);
            }
        }
        cardsFiltered = resultList;
    }

}
