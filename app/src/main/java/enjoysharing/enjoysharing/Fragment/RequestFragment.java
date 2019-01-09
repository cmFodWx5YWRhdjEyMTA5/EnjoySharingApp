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
    protected FragmentBase currentFragment;
    // Alla selezione di un tab vengono caricati anche il precedente ed il successivo
    // quindi la funzionalità la metto in un metodo a parte!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_request, container, false);
        business = new BusinessBase(activity);
        CreateFragments();
        CreateNavigationElements();
        return vMain;
    }
    @Override
    public void StartFragment()
    {
        nav_menu_request.setSelectedItemId(R.id.nav_recived_request_home);
    }
    public void ReloadCurrentTab()
    {
        SetFragment(currentFragment);
    }
    // Used to create Navigation Elements
    protected void CreateNavigationElements()
    {
        nav_menu_request = (BottomNavigationView) vMain.findViewById(R.id.nav_request);
        nav_menu_request.setOnNavigationItemSelectedListener(tabSelected);
        HideIcons();
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
    protected void CreateFragments()
    {
        recivedRequestFragment = new RecivedRequestFragment();
        recivedRequestFragment.SetActivity(activity);
        recivedRequestFragment.setFormView((FrameLayout) vMain.findViewById(R.id.request_form));
        recivedRequestFragment.setProgressView((View)vMain.findViewById(R.id.request_progress));
        sendRequestFragment = new SendRequestFragment();
        sendRequestFragment.SetActivity(activity);
        sendRequestFragment.setFormView((FrameLayout) vMain.findViewById(R.id.request_form));
        sendRequestFragment.setProgressView((View)vMain.findViewById(R.id.request_progress));
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
        currentFragment = fragment;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.request_form, fragment);
        transaction.commit();
        fragment.StartFragment();
    }

}
