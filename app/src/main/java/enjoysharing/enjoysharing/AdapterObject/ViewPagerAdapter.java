package enjoysharing.enjoysharing.AdapterObject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import enjoysharing.enjoysharing.Fragment.FragmentBase;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public List<FragmentBase> fragmentBaseList;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentBaseList = new ArrayList<>();
    }

    public List<FragmentBase> List() { return fragmentBaseList; }
    public void AddFragment(FragmentBase fragment)
    {
        fragmentBaseList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentBaseList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentBaseList.size();
    }
}
