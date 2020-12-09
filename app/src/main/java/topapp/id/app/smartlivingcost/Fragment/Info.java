package topapp.id.app.smartlivingcost.Fragment;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import topapp.id.app.smartlivingcost.Activity.Reported;
import topapp.id.app.smartlivingcost.Adapter.InfoPagerAdapter;
import topapp.id.app.smartlivingcost.R;
import topapp.id.app.smartlivingcost.Util.SessionManager;

public class Info extends Fragment {
    private SessionManager sessionManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        final TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
        final ViewPager viewPager = rootView.findViewById(R.id.pager);
        sessionManager = new SessionManager(getActivity());

        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.buttonHighlight));

        InfoPagerAdapter pagerAdapter = new InfoPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        //Memasang Adapter pada ViewPager
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Dipanggil ketika tab memasuki state/keadaan yang dipilih.
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Dipanggil saat tab keluar dari keadaan yang dipilih.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Dipanggil ketika tab yang sudah dipilih, dipilih lagi oleh user.
            }
        });


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info, menu);
        MenuItem reported = menu.findItem(R.id.reported);
       if (sessionManager.getIsAdmin().equals("0")){
           reported.setVisible(false);
       }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.reported) {
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), Reported.class));
            return true;
        }
        return false;
    }


}
