package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import enjoysharing.enjoysharing.R;

public class GalleryFragment extends FragmentBase {

    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_gallery, container, false);
        //tableHomeCards = (TableLayout) vMain.findViewById(R.id.tableHomeCards);
        setFormView((FrameLayout) vMain.findViewById(R.id.main_frame_gallery));
        super.onCreateView(inflater,container,savedInstanceState);
        return vMain;
    }

    @Override
    protected void ShowProgress(boolean state)
    {
        ShowProgressPassView(state);
    }

}
