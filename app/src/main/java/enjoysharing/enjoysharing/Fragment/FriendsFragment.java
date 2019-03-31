package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import enjoysharing.enjoysharing.R;

public class FriendsFragment extends FragmentBase {

    protected TableLayout tableFriends;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_friends, container, false);
        tableFriends = (TableLayout) vMain.findViewById(R.id.tableFriends);
        setTableReloadScrollView((ScrollView)vMain.findViewById(R.id.tableFriendsScrollView));
        reloadOnSwipeBottom = true;
        setFormView((FrameLayout) vMain.findViewById(R.id.main_frame_friends));
        super.onCreateView(inflater,container,savedInstanceState);
        return vMain;
    }
    @Override
    protected boolean CheckForCurrentFragment() { return activity.getCurrentMenuPosition()==2; }
    // Used for functionality
    public void StartFragment()
    {
        super.StartFragment();
    }

    @Override
    protected void ShowProgress(boolean state)
    {
        ShowProgressPassView(state);
    }
}
