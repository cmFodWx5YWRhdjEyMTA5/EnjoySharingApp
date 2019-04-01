package enjoysharing.enjoysharing.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;

import enjoysharing.enjoysharing.R;

public class NotifierFragment extends FragmentBase {

    protected TableLayout tableNotifier;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_notifier, container, false);
        tableNotifier = (TableLayout) vMain.findViewById(R.id.tableNotifier);
        setTableReloadScrollView((ScrollView)vMain.findViewById(R.id.tableNotifierScrollView));
        reloadOnSwipeBottom = true;
        setFormView((FrameLayout) vMain.findViewById(R.id.main_frame_notifier));
        super.onCreateView(inflater,container,savedInstanceState);
        return vMain;
    }
    @Override
    protected boolean CheckForCurrentFragment() { return activity.getCurrentMenuPosition()==3; }
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
