package enjoysharing.enjoysharing.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.R;

public class RequestFragment extends FragmentBase {

    protected BottomNavigationView nav_menu_request;
    // Request Fragments object
    protected RecivedRequestFragment recivedRequestFragment;
    protected SendRequestFragment sendRequestFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_request, container, false);
        CreateFragments(v);
        CreateNavigationElements(v);
        business = new BusinessBase(activity);
        return v;
    }
    // Used to create Navigation Elements
    protected void CreateNavigationElements(View v)
    {
        nav_menu_request = (BottomNavigationView) v.findViewById(R.id.nav_request);
        nav_menu_request.setOnNavigationItemSelectedListener(tabSelected);
        HideIcons();
        nav_menu_request.setSelectedItemId(R.id.nav_recived_request_home);
    }
    // Used to hide icons on tabs
    protected void HideIcons()
    {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) nav_menu_request.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            iconView.setVisibility(View.GONE);
        }
        menuView.setY(-40);
    }
    // Used to create fragments
    protected void CreateFragments(View v)
    {
        recivedRequestFragment = new RecivedRequestFragment();
        recivedRequestFragment.SetActivity(activity);
        recivedRequestFragment.setFormView((FrameLayout) v.findViewById(R.id.request_form));
        recivedRequestFragment.setProgressView((View)v.findViewById(R.id.request_progress));
        sendRequestFragment = new SendRequestFragment();
        sendRequestFragment.SetActivity(activity);
        sendRequestFragment.setFormView((FrameLayout) v.findViewById(R.id.request_form));
        sendRequestFragment.setProgressView((View)v.findViewById(R.id.request_progress));
    }
    // Used when user click in tab menu request
    protected BottomNavigationView.OnNavigationItemSelectedListener tabSelected
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_recived_request_home:
                    SetFragment(recivedRequestFragment);
                    break;
                case R.id.nav_send_request_requests:
                    SetFragment(sendRequestFragment);
                    break;
            }
            return true;
        }

    };

    protected void SetFragment(FragmentBase fragment)
    {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.request_form, fragment);
        transaction.commit();
    }

}
