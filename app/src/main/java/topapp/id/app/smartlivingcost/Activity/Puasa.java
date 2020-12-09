package topapp.id.app.smartlivingcost.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import topapp.id.app.smartlivingcost.Fragment.DBSLC;
import topapp.id.app.smartlivingcost.Model.PuasaModel;
import topapp.id.app.smartlivingcost.R;

public class Puasa extends AppCompatActivity {
    private DBSLC MyDatabase;
    List<PuasaModel> myPuasa = new ArrayList<>();
    private TextView tvnodata;
    ScrollView sv;
    CalendarView calendarView;
    List<EventDay> events = new ArrayList<>();
    TextView tv_hari_puasa, tv_penghematan;
    int harian;
    NumberFormat nbFmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puasa);
        final Toolbar toolbar = findViewById(R.id.toolbar); //Inisialisasi dan Implementasi id Toolbar
        setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi
        toolbar.setNavigationIcon(R.drawable.ic_back); // Set the icon
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Puasa.this, MainActivity.class);
                i.putExtra("frgToLoad", "");
                startActivity(i);
                finish();
            }
        });
        MyDatabase = new DBSLC(getApplicationContext());
        tvnodata = findViewById(R.id.tv_nodata);
        sv = findViewById(R.id.sv);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        harian = b.getInt("harian");
        tv_hari_puasa = findViewById(R.id.tv_hari_puasa);
        tv_penghematan = findViewById(R.id.tv_penghematan);
        Locale localeID = new Locale("in", "ID");
        nbFmt = NumberFormat.getCurrencyInstance(localeID);



    }

    void getData() throws ParseException {
        myPuasa.clear();

        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT * FROM " + DBSLC.Puasa.NamaTabelPuasa + " WHERE TanggalPuasa BETWEEN date('now') AND date('now','start of month','+1 month','-1 day')", null);
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
            PuasaModel puasaModel = new PuasaModel(cursor.getInt(0), cursor.getInt(1),
                    cursor.getString(2));
            myPuasa.add(puasaModel);
        }

        if (myPuasa.isEmpty()) {
            nodata();
        } else {
            for (int i = 0; i <= myPuasa.size() - 1; i++) {
                if (myPuasa.get(i).getJenis() == 1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(myPuasa.get(i).getTanggal());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    events.add(new EventDay(cal, R.drawable.ic_circle_puasa_senin_kamis));
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
                    Date date = sdf.parse(myPuasa.get(i).getTanggal());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    events.add(new EventDay(cal, R.drawable.ic_circle_puasa_ayamulbid));
                }

            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            getData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setEvents(events);
        tv_penghematan.setText(String.valueOf(nbFmt.format((-1 * harian)*myPuasa.size())));
        tv_hari_puasa.setText(String.valueOf(myPuasa.size()));
    }

    void nodata() {
        sv.setVisibility(View.GONE);
        tvnodata.setVisibility(View.VISIBLE);
    }
}
