package topapp.id.app.smartlivingcost.Activity;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.osmdroid.config.Configuration;

import java.util.Calendar;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import topapp.id.app.smartlivingcost.Fragment.Beranda;
import topapp.id.app.smartlivingcost.Fragment.DBSLC;
import topapp.id.app.smartlivingcost.Fragment.Info;
import topapp.id.app.smartlivingcost.Fragment.Input;
import topapp.id.app.smartlivingcost.Fragment.Makan;
import topapp.id.app.smartlivingcost.Fragment.Transaksi;
import topapp.id.app.smartlivingcost.R;
import topapp.id.app.smartlivingcost.Util.AppReceiver;
import topapp.id.app.smartlivingcost.Util.SessionManager;

public class MainActivity extends AppCompatActivity {

    public static DBSLC dbHelper;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private FloatingActionButton fab;
    static BottomNavigationViewEx bnve;
    FrameLayout frameLayout;
    Fragment Beranda, Transaksi, Makan, Input, Info;
    SessionManager sessionManager;
    private static final int ALARM_REQUEST_CODE = 134;
    Menu menu;


    private BottomNavigationViewEx.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationViewEx.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.i_beranda:
                    setFragment(Beranda);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                    fab.setColorFilter(getResources().getColor(R.color.colorGray));
                    bnve.setItemIconTintList(getResources().getColorStateList(R.color.selector_item_color1));
                    bnve.setItemTextColor(getResources().getColorStateList(R.color.selector_item_color1));
                    return true;
                case R.id.i_transaksi:
                    setFragment(Transaksi);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                    fab.setColorFilter(getResources().getColor(R.color.colorGray));
                    bnve.setItemIconTintList(getResources().getColorStateList(R.color.selector_item_color1));
                    bnve.setItemTextColor(getResources().getColorStateList(R.color.selector_item_color1));
                    return true;
                case R.id.i_input:
                    setFragment(Input);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                    fab.setColorFilter(getResources().getColor(R.color.white));
                    bnve.setItemIconTintList(getResources().getColorStateList(R.color.selector_item_color1));
                    bnve.setItemTextColor(getResources().getColorStateList(R.color.selector_item_color1));
                    return true;
                case R.id.i_makan:
                    setFragment(Makan);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                    fab.setColorFilter(getResources().getColor(R.color.colorGray));
                    bnve.setItemIconTintList(getResources().getColorStateList(R.color.selector_item_color2));
                    bnve.setItemTextColor(getResources().getColorStateList(R.color.selector_item_color2));
                    return true;
                case R.id.i_info:
                    setFragment(Info);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                    fab.setColorFilter(getResources().getColor(R.color.colorGray));
                    bnve.setItemIconTintList(getResources().getColorStateList(R.color.selector_item_color2));
                    bnve.setItemTextColor(getResources().getColorStateList(R.color.selector_item_color2));

                    return true;
                default:
                    return false;
            }
        }
    };

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);

        bnve = findViewById(R.id.bnve);
        frameLayout = findViewById(R.id.frame_layout);
        Beranda = new Beranda();
        Transaksi = new Transaksi();
        Input = new Input();
        Makan = new Makan();
        Info = new Info();

        bnve.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        dbHelper = new DBSLC(context);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performInputClick();
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                fab.setColorFilter(getResources().getColor(R.color.white));
            }
        });

        String intentFragment = Objects.requireNonNull(getIntent().getExtras()).getString("frgToLoad");
        switch (Objects.requireNonNull(intentFragment)) {
            case "Transaksi":
                setFragment(Transaksi);
                bnve.setSelectedItemId(R.id.i_transaksi);
                break;
            case "Makan":
                setFragment(Makan);
                bnve.setSelectedItemId(R.id.i_makan);
                break;
            case "Input":
                setFragment(Input);
                bnve.setSelectedItemId(R.id.i_input);
                break;
            default:
                setFragment(Beranda);
                break;
        }

        sessionManager = new SessionManager(this);
        startAlarmManager();

    }


    public static void performInputClick() {
        View view = bnve.findViewById(R.id.i_input);
        view.performClick();
    }

    public static void performTransaksiClick() {
        View view = bnve.findViewById(R.id.i_transaksi);
        view.performClick();
    }

    public static void performReOpenInfo() {
        View view = bnve.findViewById(R.id.i_input);
        view.performClick();
        View view1 = bnve.findViewById(R.id.i_info);
        view1.performClick();
    }

    public void startAlarmManager() {
        Intent alarmIntent = new Intent(MainActivity.this, AppReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, ALARM_REQUEST_CODE, alarmIntent, 0);

        //set waktu sekarang berdasarkan interval
        Calendar cal = Calendar.getInstance();
        //set interval notifikasi 3600 detik = 1 jam
        int interval_hour = 1;
        cal.add(Calendar.HOUR, interval_hour);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //set alarm manager dengan memasukkan waktu yang telah dikonversi menjadi milliseconds
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        Log.d("alarmslc", "SET");
        //nambah komen

    }


    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Keluar?")
                .setContentText("Apakah anda yakin untuk keluar dari aplikasi?")
                .setCancelText("Tidak")
                .setConfirmText("Ya")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        MainActivity.this.finish();
                        Intent d = new Intent(Intent.ACTION_MAIN);
                        d.addCategory(Intent.CATEGORY_HOME);
                        d.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(d);
                    }
                })
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();

    }


}
