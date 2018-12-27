package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.R;

public class RequestFragment extends FragmentBase {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_request, container, false);
        business = new BusinessBase(activity);
        return v;
    }

}
