package suska.uin.tif.smartlivingcost.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import suska.uin.tif.smartlivingcost.Fragment.TabInfo.MyMenuFragment;
import suska.uin.tif.smartlivingcost.Fragment.TabInfo.ReviewFragment;

public class InfoPagerAdapter extends FragmentStatePagerAdapter {
    private int number_tabs;

    public InfoPagerAdapter(FragmentManager fm, int number_tabs) {
        super(fm);
        this.number_tabs = number_tabs;
    }

    //Mengembalikan Fragment yang terkait dengan posisi tertentu
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MyMenuFragment();
            case 1:
                return new ReviewFragment();
            default:
                return null;
        }
    }

    //Mengembalikan jumlah tampilan yang tersedia.
    @Override
    public int getCount() {
        return number_tabs;
    }
}
