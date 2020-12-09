package suska.uin.tif.smartlivingcost.Activity;

import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import suska.uin.tif.smartlivingcost.Adapter.CatPagerAdapter;
import suska.uin.tif.smartlivingcost.R;

public class Category extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        final Toolbar toolbar = findViewById(R.id.toolbarcat); //Inisialisasi dan Implementasi id Toolbar
        setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi

        toolbar.setNavigationIcon(R.drawable.ic_back); // Set the icon

        // Icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Menerapkan TabLayout dan ViewPager pada Activity
        final AppBarLayout appBar = findViewById(R.id.appBarcat);
        final TabLayout tabLayout = findViewById(R.id.tab_layoutcat);
        final ViewPager viewPager = findViewById(R.id.pagercat);

        Bundle bundle = getIntent().getExtras();
        int s = bundle.getInt("index");


        //Memanggil dan Memasukan Value pada Class PagerAdapter(FragmentManager dan JumlahTab)
        CatPagerAdapter pagerAdapter = new CatPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        //Memasang Adapter pada ViewPager
        viewPager.setAdapter(pagerAdapter);

        /*
         Menambahkan Listener yang akan dipanggil kapan pun halaman berubah atau
         bergulir secara bertahap, sehingga posisi tab tetap singkron
         */

        getWindow().setStatusBarColor(ContextCompat.getColor(Category.this, R.color.buttonHighlight));
        toolbar.setBackgroundColor(getResources().getColor(R.color.RedTheme));
        tabLayout.setBackgroundColor(getResources().getColor(R.color.RedTheme));
        toolbar.setTitle("Pengaturan Kategori");
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Callback Interface dipanggil saat status pilihan tab berubah.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Dipanggil ketika tab memasuki state/keadaan yang dipilih.
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    getWindow().setStatusBarColor(ContextCompat.getColor(Category.this, R.color.colorPrimaryDark));
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    getWindow().setStatusBarColor(ContextCompat.getColor(Category.this, R.color.buttonHighlight));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.RedTheme));
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.RedTheme));

                }
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


        viewPager.setCurrentItem(s);
    }

}