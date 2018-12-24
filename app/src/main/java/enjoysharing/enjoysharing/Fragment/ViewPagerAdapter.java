package enjoysharing.enjoysharing.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public List<FragmentBase> fragmentBaseList;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentBaseList = new ArrayList<>();
    }

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
