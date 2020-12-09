package suska.uin.tif.smartlivingcost.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maltaisn.icondialog.IconHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.abhinay.input.CurrencyEditText;
import suska.uin.tif.smartlivingcost.Adapter.SelectCategoryRVItem;
import suska.uin.tif.smartlivingcost.Fragment.DBSLC;
import suska.uin.tif.smartlivingcost.Model.KategoriModel;
import suska.uin.tif.smartlivingcost.R;
import suska.uin.tif.smartlivingcost.Util.SessionManager;

import static android.content.ContentValues.TAG;

public class RecomSetting extends AppCompatActivity {
    private DBSLC MyDatabase;
    private String rcNamaKat, rcIconKat, rcIdKat, rcJenisKat;
    SelectCategoryRVItem SCRVI;
    RelativeLayout rlcat;
    RecyclerView recyclerView;
    ImageView iconcat;
    TextView namaCat;
    SessionManager sessionManager;
    IconHelper iconHelper;
    ImageView catset;
    Button myResto;
    EditText nama_menu;
    CurrencyEditText jarak_resto, baris;
    private List<KategoriModel> myKategori = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recom_setting);
        final Toolbar toolbar = findViewById(R.id.toolbar); //Inisialisasi dan Implementasi id Toolbar
        setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));
        toolbar.setNavigationIcon(R.drawable.ic_back); // Set the icon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(baris.getText()).toString().equals("")) {
                    sessionManager.setReq_row("0");
                } else {
                    sessionManager.setReq_row(String.valueOf(baris.getCleanIntValue()));
                }

                if (Objects.requireNonNull(jarak_resto.getText()).toString().equals("")) {
                    sessionManager.setReq_jarak("0");
                } else {
                    sessionManager.setReq_jarak(String.valueOf(jarak_resto.getCleanIntValue()));
                }
                sessionManager.setReq_nama(nama_menu.getText().toString());

                Intent i = new Intent(RecomSetting.this, MainActivity.class);
                i.putExtra("frgToLoad", "Makan");
                // Now start your activity
                startActivity(i);
                RecomSetting.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();

            }
        });

        initiateComponent();
        createrv();
        selectCategory();

    }

    private void initiateComponent() {
        MyDatabase = new DBSLC(getBaseContext());
        sessionManager = new SessionManager(this);
        getData();
        SCRVI = new SelectCategoryRVItem(myKategori);
        recyclerView = findViewById(R.id.select_category);
        rlcat = findViewById(R.id.rlcat);

        iconHelper = IconHelper.getInstance(this);
        iconcat = findViewById(R.id.imv_iconcat);
        namaCat = findViewById(R.id.tv_catname);
        catset = findViewById(R.id.btnsetcat);
        myResto = findViewById(R.id.myResto);
        nama_menu = findViewById(R.id.dialogNamaPencarian);
        jarak_resto = findViewById(R.id.dialogJarakPencarian);
        baris = findViewById(R.id.dialogBarisPencarian);

        jarak_resto.setSeparator(".");
        baris.setSeparator(".");
        jarak_resto.setDecimals(false);
        baris.setDecimals(false);


        if (MyDatabase.isKategoriexist(sessionManager.getId_kat_recom()) == 0) {
            sessionManager.setId_icon_kat_recom("");
            sessionManager.setId_kat_recom("");
            sessionManager.setNama_kat_recom("");
        }

        if (!sessionManager.getReq_nama().equals("")) {
            nama_menu.setText(sessionManager.getReq_nama());
        }
        if (!sessionManager.getReq_jarak().equals("")) {
            jarak_resto.setText(sessionManager.getReq_jarak());
        }
        if (!sessionManager.getReq_row().equals("")) {
            baris.setText(sessionManager.getReq_row());
        }

        myResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecomSetting.this, MyResto.class));
            }
        });
    }

    private void selectCategory() {
        iconcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewrlcat();

            }
        });
        namaCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewrlcat();

            }
        });

        SCRVI.setOnItemClickListener(new SelectCategoryRVItem.ClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                Log.d(TAG, "onItemClick position: " + rcIdKat + rcIconKat + rcNamaKat + rcJenisKat);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rcIconKat = myKategori.get(position).getIdIconKat();
                        rcIdKat = myKategori.get(position).getIdKat();
                        rcNamaKat = myKategori.get(position).getNamaKat();
                        rcJenisKat = myKategori.get(position).getJenisKat();
                        namaCat.setText(rcNamaKat);
                        rlcat.setVisibility(View.GONE);
                        sessionManager.setId_kat_recom(rcIdKat);
                        sessionManager.setNama_kat_recom(rcNamaKat);
                        sessionManager.setId_icon_kat_recom(rcIconKat);
                        iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
                            @Override
                            public void onDataLoaded() {
                                // This happens on UI thread, and is guaranteed to be called.
                                iconcat.setImageDrawable(iconHelper.getIcon(Integer.parseInt(rcIconKat)).getDrawable(RecomSetting.this));
                            }

                        });
                    }
                });

            }
        });

        if (!sessionManager.getId_kat_recom().equals("")) {
            iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
                @Override
                public void onDataLoaded() {
                    iconcat.setImageDrawable(iconHelper.getIcon(Integer.parseInt(sessionManager.getId_icon_kat_recom())).getDrawable(RecomSetting.this));
                    namaCat.setText(sessionManager.getNama_kat_recom());
                }
            });
        }
        catset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecomSetting.this, Category.class);
                i.putExtra("index", 0);
                startActivity(i);
            }
        });
    }

    private void viewrlcat() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rlcat.getVisibility() == View.VISIBLE) {
                    rlcat.setVisibility(View.GONE);
                } else {
                    rlcat.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void createrv() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(RecomSetting.this, 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(SCRVI);
    }

    public void onBackPressed() {
        if (Objects.requireNonNull(baris.getText()).toString().equals("")) {
            sessionManager.setReq_row("0");
        } else {
            sessionManager.setReq_row(String.valueOf(baris.getCleanIntValue()));
        }

        if (Objects.requireNonNull(jarak_resto.getText()).toString().equals("")) {
            sessionManager.setReq_jarak("0");
        } else {
            sessionManager.setReq_jarak(String.valueOf(jarak_resto.getCleanIntValue()));
        }
        sessionManager.setReq_nama(nama_menu.getText().toString());
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("frgToLoad", "Makan");
        // Now start your activity
        startActivity(i);
        RecomSetting.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();

    }

    @SuppressLint("Recycle")
    private void getData() {
        myKategori.clear();
        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT * FROM " + DBSLC.MyColumns.NamaTabelKat + " WHERE " + DBSLC.MyColumns.JenisKat + "=" + "0", null);
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
            KategoriModel kategoriModel = new KategoriModel(cursor.getString(0), cursor.getString(1)
                    , cursor.getString(2), cursor.getString(3), cursor.getString(4));
            myKategori.add(kategoriModel);


        }
    }


}
