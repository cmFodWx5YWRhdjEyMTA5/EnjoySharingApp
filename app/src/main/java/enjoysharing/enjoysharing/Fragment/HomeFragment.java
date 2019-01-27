package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.support.v7.widget.TooltipCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import enjoysharing.enjoysharing.Activity.CardDetailActivity;
import enjoysharing.enjoysharing.Activity.RequestListActivity;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.Business.BusinessJSON;
import enjoysharing.enjoysharing.DataObject.CardCollection;
import enjoysharing.enjoysharing.DataObject.CardHome;
import enjoysharing.enjoysharing.R;

public class HomeFragment extends FragmentBase {

    protected TableLayout tableHomeCards;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_home, container, false);
        tableHomeCards = (TableLayout) vMain.findViewById(R.id.tableHomeCards);
        setFormView((FrameLayout) vMain.findViewById(R.id.main_frame_home));
        super.onCreateView(inflater,container,savedInstanceState);
        return vMain;
    }
    @Override
    protected void ShowProgress(boolean state)
    {
        activity.showProgress(state, formView, progressView);
    }
    @Override
    public void StartFragment()
    {
        business = new BusinessBase(activity);
        FillHomeCards();
    }

    protected CardCollection homeCards;
    // User to load home cards
    // TODO
    // Fill cards by server call
    protected void FillHomeCards()
    {
        if (mTask != null) {
            homeCards = new CardCollection();
            DrawCardsOnTable(homeCards,tableHomeCards);
            return;
        }
        //ShowProgress(true);
        mTask = new FragmentRequestTask(false, true, "EventServlet");
        mTask.AddParameter("RequestType","H");
        try
        {
            mTask.execute();
        }
        catch (Exception e)
        {
            activity.retObj.setStateResponse(false);
            activity.retObj.setMessage("GeneralError");
        }
    }
    // TODO
    // Server call
    @Override
    protected void DoInBackground()
    {
        if(activity.simulateCall)
            homeCards = business.GetHomeCards(null);
        else
            homeCards = new BusinessJSON(activity).GetHomeCards(activity.retObj.getMessage());
    }

    @Override
    protected void OnRequestPostExecute()
    {
        if(requestSuccess && activity.retObj.isOkResponse())
        {
            if(homeCards != null)
                // Riempio la tabella qui perchè altrimenti mi dice che non posso accedere alla view da un task che non è l'originale
                DrawCardsOnTable(homeCards,tableHomeCards);
        }
        else
            Toast.makeText(activity,activity.retObj.getMessage(), Toast.LENGTH_SHORT).show();
    }

    protected void DrawCardsOnTable(CardCollection cards, TableLayout table)
    {
        table.removeAllViews();
        int txtUserTitleWidth = business.ConvertWidthBasedOnPerc(85);
        int parentTollerancePX = 5;
        for (int i=0; i<cards.List().size(); i++) {
            final CardHome card = (CardHome)cards.List().get(i);
            TableRow row = (TableRow) LayoutInflater.from(activity).inflate(R.layout.card_home, null);
            LinearLayout relLayout = (LinearLayout)row.getChildAt(0);
            // row.getChildAt(0) è il relative layout che contiene tutti gli elementi
            TextView txtUserCardHome = (TextView)relLayout.findViewById(R.id.txtUserCardHome);
            // Set width based on screen percentage
            txtUserCardHome.setWidth(txtUserTitleWidth);
            txtUserCardHome.setText(card.getUserName());
            TextView txtTitleCardHome = (TextView)relLayout.findViewById(R.id.txtTitleCardHome);
            // Set width based on screen percentage
            txtTitleCardHome.setWidth(txtUserTitleWidth);
            txtTitleCardHome.setText(card.getTitle());
            TextView txtContentCardHome = (TextView)relLayout.findViewById(R.id.txtContentCardHome);
            // Set the same width of parent - tollerance
            txtContentCardHome.setWidth(((LinearLayout)txtContentCardHome.getParent()).getWidth()-parentTollerancePX);
            txtContentCardHome.setText(card.getContent());
            final TextView txtNumberPerson = (TextView)relLayout.findViewById(R.id.txtNumberPerson);
            txtNumberPerson.setText(card.getAcceptedRequest() + "/" + card.getMaxRequest());
            txtNumberPerson.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Open list of persons
                    OpenRequestList(activity.getBaseContext(),RequestListActivity.class, card, false);
                }
            });
            ImageView imgBtnGender = (ImageView)relLayout.findViewById(R.id.imgBtnGender);
            imgBtnGender.setImageResource(business.GetGenderIcon(card.getGenderEventId()));
            TooltipCompat.setTooltipText(imgBtnGender, business.GetGenderItem(card.getGenderEventId()));

            Button btnPartecipateRequest = (Button)relLayout.findViewById(R.id.btnPartecipateRequest);
            business.SetButtonRequest(btnPartecipateRequest,true);
            btnPartecipateRequest.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRequestPartecipate(v);
                }
            });

            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRowClick(v, card.getEventId());
                }
            });
            table.addView(row);
        }
    }

    @Override
    protected void onRowClick(View v, int EventId)
    {
        CardHome card = (CardHome) homeCards.GetCard(EventId);
        if(card != null)
        {
            SwipeDownOpenActivity(activity.getBaseContext(), CardDetailActivity.class, card);
        }
    }

}
