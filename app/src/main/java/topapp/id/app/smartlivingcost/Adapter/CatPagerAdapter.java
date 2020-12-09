package topapp.id.app.smartlivingcost.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import topapp.id.app.smartlivingcost.Fragment.CatIncome;
import topapp.id.app.smartlivingcost.Fragment.CatOutcome;

public class CatPagerAdapter extends FragmentStatePagerAdapter {
    private int number_tabs;

    public CatPagerAdapter(FragmentManager fm, int number_tabs) {
        super(fm);
        this.number_tabs = number_tabs;
    }

    //Mengembalikan Fragment yang terkait dengan posisi tertentu
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CatOutcome();
            case 1:
                return new CatIncome();
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
