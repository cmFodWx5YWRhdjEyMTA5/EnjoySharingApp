package enjoysharing.enjoysharing.Business;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import enjoysharing.enjoysharing.Activity.BaseActivity;
import enjoysharing.enjoysharing.DataObject.Card.CardBase;
import enjoysharing.enjoysharing.DataObject.Card.CardCollection;
import enjoysharing.enjoysharing.DataObject.Card.CardHome;
import enjoysharing.enjoysharing.DataObject.Card.CardMyEvent;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestSent;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestRecived;
import enjoysharing.enjoysharing.DataObject.Card.CardRequestUserListCollection;
import enjoysharing.enjoysharing.DataObject.ParameterCollection;
import enjoysharing.enjoysharing.DataObject.RequestUser;
import enjoysharing.enjoysharing.DataObject.UserCollection;
import enjoysharing.enjoysharing.R;

public class BusinessBase {

    public static class Constants
    {
        public static String DateTimeFormatJSON_IN = "yyyy-MM-dd HH:mm:ss.S";
        public static String DateTimeFormatJSON_OUT = "EEE dd/MM/yyyy"+System.getProperty("line.separator")+"HH:mm";
        public static String DateTimeFormatJSON_OUT_SHORT = "yyyy-MM-dd HH:mm";
    }

    protected BaseActivity activity;

    public BusinessBase() { }

    public BusinessBase(BaseActivity activity)
    {
        this.activity = activity;
    }

    public Bitmap StringToImage(String base64)
    {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);//base64.getBytes();//Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public String ImageToString(Bitmap imgBitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public void LoadUserImage(ImageView img)
    {
        LoadImage(img,activity.GetCurrentUserImage());
    }

    public void LoadImage(ImageView img, Bitmap imageBitmap)
    {
        if(imageBitmap != null)
            img.setImageBitmap(imageBitmap);
        // TODO
        // Se manca immagine -> immagine default
    }

    public void LoadImage(ImageView img, String strBase64)
    {
        if(strBase64 != null && !strBase64.equals(""))
            img.setImageBitmap(StringToImage(strBase64));
        // TODO
        // Se manca immagine -> immagine default
    }

    public String decrypt(String str)
    {
        byte[] valueDecoded= new byte[0];
        try {
            valueDecoded = Base64.decode(str.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (Exception e) {
        }
        return new String(valueDecoded);
    }

    public String encrypt(String str)
    {
        String base64 = null;
        try {
            byte[] data = str.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
        }
        return base64.replace("\n","");
    }

    // Used to check if text is truncate
    public boolean isTextTruncated(TextView textView)
    {
        String text = textView.getText().toString();
        if ( textView != null && text != null )
        {

            Layout layout = textView.getLayout();
            if ( layout != null )
            {
                int lines = layout.getLineCount();
                if ( lines > 0 )
                {
                    int ellipsisCount = layout.getEllipsisCount( lines - 1 );
                    if ( ellipsisCount > 0 )
                    {
                        return true;
                    }
                }
            }

        }
        return false;
    }
    public Date GetDate(String dateStr)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateTimeFormatJSON_OUT_SHORT);
            return sdf.parse(dateStr);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    public String GetDateString(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateTimeFormatJSON_OUT);
        return sdf.format(date);
    }
    public String GetDateStringShort(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateTimeFormatJSON_OUT_SHORT);
        return sdf.format(date);
    }
    // Used when simulate
    public UserCollection GetRequestList(String JSONStr)
    {
        UserCollection users = new UserCollection();
        int idCard = 0;
        users.Add(new RequestUser(idCard++,"Utente 1",1));
        users.Add(new RequestUser(idCard++,"Utente 2",0));
        users.Add(new RequestUser(idCard++,"Utente 3",1));
        users.Add(new RequestUser(idCard++,"Utente 4",2));
        users.Add(new RequestUser(idCard++,"Utente 5",2));
        users.Add(new RequestUser(idCard++,"Utente 6",0));
        return users;
    }
    // Used when simulate
    public CardCollection GetHomeCards(String JSONStr)
    {
        CardCollection cards = new CardCollection();
        int idEvent = 0;
        Date EventDate = Calendar.getInstance().getTime();
        cards.Add(new CardHome(idEvent++, 1,"Utente 1","Titolo 1","Contenuto di prova 1 caricato da codice, proviamo a vedere come viene il testo sfumato in fondo alla text view...mah! Ho letto che non si può fare, però la credo difficile, stiamo a vedere!",null, 1, 1, 5,false,EventDate));
        cards.Add(new CardHome(idEvent++,2,"Utente 2","Titolo 2","Contenuto di prova 2 caricato da codice",null, 3, 4, 4,true,EventDate));
        cards.Add(new CardHome(idEvent++,3,"Utente 3","Titolo 3 moooooooooooollllllltttttoooooooo lllllluuuuuuunnnnnnngggggggggggooooooooo","Contenuto di prova 3 caricato da codice",null, 2, 5, 7,false,EventDate));
        cards.Add(new CardHome(idEvent++,4,"Utente 4","Titolo 4","Contenuto di prova 4 caricato da codice",null, 4, 9, 10,true,EventDate));
        return cards;
    }
    // Used when simulate
    public CardCollection GetMyEventsCards(String JSONStr)
    {
        CardCollection cards = new CardCollection();
        int idEvent = 0;
        Date EventDate = Calendar.getInstance().getTime();
        cards.Add(new CardMyEvent(idEvent++,activity.GetUser().getUserId(), activity.GetUser().getUsername(),"Titolo 1","Contenuto di prova 1 caricato da codice, proviamo a vedere come viene il testo sfumato in fondo alla text view...mah! Ho letto che non si può fare, però la credo difficile, stiamo a vedere!",null, 1, 1, 5,EventDate));
        cards.Add(new CardMyEvent(idEvent++,activity.GetUser().getUserId(),activity.GetUser().getUsername(),"Titolo 2","Contenuto di prova 2 caricato da codice",null, 3, 6, 6,EventDate));
        cards.Add(new CardMyEvent(idEvent++,activity.GetUser().getUserId(),activity.GetUser().getUsername(),"Titolo 3 moooooooooooollllllltttttoooooooo lllllluuuuuuunnnnnnngggggggggggooooooooo","Contenuto di prova 3 caricato da codice",null, 2, 4, 6,EventDate));
        cards.Add(new CardMyEvent(idEvent++,activity.GetUser().getUserId(),activity.GetUser().getUsername(),"Titolo 4","Contenuto di prova 4 caricato da codice",null, 4, 9, 10,EventDate));
        return cards;
    }
    // Remove when call server is ok
    public CardCollection GetRequestSentCards(String JSONStr)
    {
        CardCollection cards = new CardCollection();
        int idEvent = 0;
        Date EventDate = Calendar.getInstance().getTime();
        cards.Add(new CardRequestSent(idEvent++,1,"Utente 1","Titolo 1","Contenuto di prova 1 caricato da codice, proviamo a vedere come viene il testo sfumato in fondo alla text view...mah! Ho letto che non si può fare, però la credo difficile, stiamo a vedere!",null, 1, 1, 5,2,EventDate));
        cards.Add(new CardRequestSent(idEvent++,2,"Utente 2","Titolo 2","Contenuto di prova 2 caricato da codice",null, 3, 4, 4,2,EventDate));
        cards.Add(new CardRequestSent(idEvent++,3,"Utente 3","Titolo 3 moooooooooooollllllltttttoooooooo lllllluuuuuuunnnnnnngggggggggggooooooooo","Contenuto di prova 3 caricato da codice",null, 2, 5, 7,2,EventDate));
        cards.Add(new CardRequestSent(idEvent++,4,"Utente 4","Titolo 4","Contenuto di prova 4 caricato da codice",null, 4, 9, 10,2,EventDate));
        return cards;
    }
    // Used when simulate
    public CardCollection GetRequestRecivedCards(String JSONStr)
    {
        CardCollection cards = new CardCollection();
        Date EventDate = Calendar.getInstance().getTime();
        cards.Add(new CardRequestRecived(1,1,"Utente 1","Titolo 1","Contenuto di prova 1 caricato da codice, proviamo a vedere come viene il testo sfumato in fondo alla text view...mah! Ho letto che non si può fare, però la credo difficile, stiamo a vedere!",null, 1, 1, 5,EventDate));
        cards.Add(new CardRequestRecived(1,2,"Utente 2","Titolo 2","Contenuto di prova 2 caricato da codice",null, 3, 4, 4,EventDate));
        cards.Add(new CardRequestRecived(2,3,"Utente 3","Titolo 3 moooooooooooollllllltttttoooooooo lllllluuuuuuunnnnnnngggggggggggooooooooo","Contenuto di prova 3 caricato da codice",null, 2, 5, 7,EventDate));
        cards.Add(new CardRequestRecived(1,4,"Utente 4","Titolo 4","Contenuto di prova 4 caricato da codice",null, 4, 9, 10,EventDate));
        return cards;
    }
    // Used to convert width based on percentage
    public int ConvertWidthBasedOnPerc(int perc)
    {
        int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        return (screenWidth/100)*perc;
    }
    // Used to manage button request partecipate
    public void SetButtonRequest(Button btn, boolean state)
    {
        if(state)
        {
            btn.setText(R.string.txtRequestPartecipate);
            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_request_custom,0,0,0);
        }
        else
        {
            btn.setText(R.string.txtRequestPartecipateReverse);
            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_request_partecipate_reverse,0,0,0);
        }
        btn.setHint(state?"1":"0");
    }
    // Used to disable request button
    public void DisableRequestButton(Button btn)
    {
        btn.setEnabled(false);
        btn.setText(R.string.txtRequestPartecipateReversed);
        btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_request_disabled_custom,0,0,0);
        btn.setHint("0");
    }
    // Used to set a non clickable button
    public void setButtonNotClickable(Button btn)
    {
        btn.setEnabled(false);
        btn.setText(R.string.txtDisablePatecipation);
        btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_request_disabled_custom,0,0,0);
    }

    // This method checks the actual date with the eventdate
    // if it's afther than the event date, it returns FALSE
    public boolean checkButtonNotClickable (Button btn, Date EventDate)
    {
        Date currentTime = Calendar.getInstance().getTime();

        if( currentTime.after(EventDate)) {
            setButtonNotClickable(btn);
            return false;
        }
        return true;
    }
    // Used when loading request
    public void LoadingRequestButton(Button btn, boolean loading)
    {
        btn.setEnabled(!loading);
        if(loading)
        {
            btn.setText(R.string.txtRequestPartecipateLoading);
            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_request_disabled_custom,0,0,0);
        }
        else
        {
            SetButtonRequest(btn,btn.getHint()=="1");
        }
    }

    public int GetGenderIconSearch(int index)
    {
        switch (index)
        {
            case 0: // Tutti
                return R.drawable.ic_gender_all_search;
            case 1: // Svago
                return R.drawable.ic_gender_svago_search;
            case 2: // Utilità
                return R.drawable.ic_gender_utility_search;
            case 3: // Intrattenimento
                return R.drawable.ic_gender_intrattenimento_search;
            case 4: // Lavoro
                return R.drawable.ic_gender_business_search;
            case 5: // Pranzo/Cena
                return R.drawable.ic_gender_food_search;
        }
        return -1;
    }

    public int GetGenderIcon(int index)
    {
        switch (index)
        {
            case 1: // Svago
                return R.drawable.ic_gender_svago;
            case 2: // Utilità
                return R.drawable.ic_gender_utility;
            case 3: // Intrattenimento
                return R.drawable.ic_gender_intrattenimento;
            case 4: // Lavoro
                return R.drawable.ic_gender_business;
            case 5: // Pranzo/Cena
                return R.drawable.ic_gender_food;
        }
        return -1;
    }
    public int GetGenderIndex(String item) { return GetSpinnerIndex(R.array.genders,item); }
    public String GetGenderItem(int index) { return GetSpinnerItem(R.array.genders,index); }
    public String[] GetGenderItems() { return GetSpinnerItems(R.array.genders); }

    public int GetGenderSearchIndex(String item) { return GetSpinnerIndex(R.array.genders_search,item); }
    public String[] GetGenderSearchItems() { return GetSpinnerItems(R.array.genders_search); }

    public int GetSpinnerIndex(int arrayName, String item)
    {
        String[] items = activity.getBaseContext().getResources().getStringArray(arrayName);
        for(int i=0;i<items.length;i++)
            if(items[i].equals(item))
                return i;
        return -1;
    }

    public String GetSpinnerItem(int arrayName, int index)
    {
        String[] items = activity.getBaseContext().getResources().getStringArray(arrayName);
        return items.length < index ? "" : items[index];
    }

    public String[] GetSpinnerItems(int arrayName)
    {
        return activity.getBaseContext().getResources().getStringArray(arrayName);
    }

    public CardRequestUserListCollection GetGroupedCards(CardCollection cards)
    {
        CardRequestUserListCollection resultCards = new CardRequestUserListCollection();
        for(CardBase c : cards.List())
        {
            resultCards.Add(c);
        }
        return resultCards;
    }

    // Used by BusinessJSON
    public ParameterCollection GetParamsByJSON(String message) {
        return null;
    }
}
