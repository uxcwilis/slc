package topapp.id.app.smartlivingcost.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maltaisn.icondialog.IconHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import me.abhinay.input.CurrencyEditText;
import topapp.id.app.smartlivingcost.Adapter.Planning_Master_RV;
import topapp.id.app.smartlivingcost.Adapter.SelectCategoryRVItem;
import topapp.id.app.smartlivingcost.Fragment.DBSLC;
import topapp.id.app.smartlivingcost.Model.KategoriModel;
import topapp.id.app.smartlivingcost.Model.PlanningModel;
import topapp.id.app.smartlivingcost.R;

public class Planning extends AppCompatActivity {
    private DBSLC MyDatabase;
    FloatingActionButton fab;
    RecyclerView recyclerView, recyclerView2;
    SimpleDateFormat dateFormatter;
    TextView tv_nodata, tv_catname;
    String tanggal, rcIconKat, rcIdKat = "", rcNamaKat, rcJenisKat;
    SelectCategoryRVItem SCRVI;
    RelativeLayout rlcat, appbarv;
    EditText namarencana, target_date;
    CurrencyEditText target_money;
    ImageView imv_iconcat, btnsetcat, icon_nodata;
    IconHelper iconHelper;
    private List<KategoriModel> myKategori = new ArrayList<>();
    private List<PlanningModel> myPlanning = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Perencanaan Masa Depan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MyDatabase = new DBSLC(getBaseContext());
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler);
        tv_nodata = findViewById(R.id.tv_nodata);
        icon_nodata = findViewById(R.id.icon_nodata);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlanning();
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fab.isShown())
                    fab.show();
                else if (dy > 0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    //Berisi Statement-Statement Untuk Mengambi Data dari Database
    @SuppressLint("Recycle")
    protected void getData() {
        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT Planning.*, Kategori.NamaKat, Kategori.IdIconKat FROM " + DBSLC.Planning.NamaTabelPlanning + " INNER JOIN Kategori ON" +
                " Planning.IdKategori = Kategori.IdKat ORDER BY DateInput DESC ", null);
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
            PlanningModel planningModel = new PlanningModel(cursor.getString(0), cursor.getString(1)
                    , cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)
                    , cursor.getString(6), cursor.getString(7));
            myPlanning.add(planningModel);
        }
    }

    void createRV() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Planning.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new Planning_Master_RV(myPlanning);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line)));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myPlanning.clear();
        getData();
        if (myPlanning.isEmpty()) {
            tv_nodata.setVisibility(View.VISIBLE);
            icon_nodata.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            tv_nodata.setVisibility(View.GONE);
            icon_nodata.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            createRV();
        }

    }

    void addPlanning() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Planning.this);
        @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.planning_master_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Planning.this);
        alertDialogBuilderUserInput.setView(mView);


        final Calendar newCalendar = Calendar.getInstance();
        tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        namarencana = mView.findViewById(R.id.nama_rencana);
        target_money = mView.findViewById(R.id.target_money);
        target_date = mView.findViewById(R.id.target_date);
        imv_iconcat = mView.findViewById(R.id.imv_iconcat);
        tv_catname = mView.findViewById(R.id.tv_catname);
        rlcat = mView.findViewById(R.id.rlcat);
        appbarv = mView.findViewById(R.id.appbarv);
        btnsetcat = mView.findViewById(R.id.btnsetcat);
        iconHelper = IconHelper.getInstance(Planning.this);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        target_money.setSeparator(".");
        target_money.setDecimals(false);


        recyclerView2 = mView.findViewById(R.id.select_category);

        getData2();
        SCRVI = new SelectCategoryRVItem(myKategori);
        SCRVI.notifyDataSetChanged();
        createrv();

        target_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Planning.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        target_date.setText(dateFormatter.format(newDate.getTime()));
                        tanggal = dateFormatter.format(newDate.getTime());
                        Log.d("isitanggal", tanggal);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }

        });

        imv_iconcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_categoryy();
            }
        });

        tv_catname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_categoryy();
            }
        });

        SCRVI.setOnItemClickListener(new SelectCategoryRVItem.ClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rcIconKat = myKategori.get(position).getIdIconKat();
                        rcIdKat = myKategori.get(position).getIdKat();
                        rcNamaKat = myKategori.get(position).getNamaKat();
                        rcJenisKat = myKategori.get(position).getJenisKat();
                        tv_catname.setText(rcNamaKat);
                        rlcat.setVisibility(View.GONE);
                        iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
                            @Override
                            public void onDataLoaded() {
                                // This happens on UI thread, and is guaranteed to be called.
                                imv_iconcat.setImageDrawable(iconHelper.getIcon(Integer.parseInt(rcIconKat)).getDrawable(Planning.this));
                            }

                        });
                    }
                });

            }
        });

        btnsetcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Planning.this, Category.class);
                i.putExtra("index", 0);
                startActivity(i);
            }
        });


        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if (namarencana.getText().toString().equals("") || namarencana.getText().toString().isEmpty()) {
                            Toast.makeText(Planning.this, "Silahkan Isi Nama Perencanaan", Toast.LENGTH_SHORT).show();
                        } else if (Objects.requireNonNull(target_money.getText()).toString().isEmpty() || target_money.getText().toString().equals("")) {
                            Toast.makeText(Planning.this, "Silahkan Isi Jumlah Uang Yang Ingin Dikumpulkan", Toast.LENGTH_SHORT).show();
                        } else if (target_date.getText().toString().equals("") || target_date.getText().toString().isEmpty()) {
                            Toast.makeText(Planning.this, "Silahkan Isi Tanggal Keperluan", Toast.LENGTH_SHORT).show();
                        } else if (rcIdKat.isEmpty()) {
                            Toast.makeText(Planning.this, "Silahkan Isi Pilih Kategori", Toast.LENGTH_SHORT).show();
                        } else {
                            savePlanning(Integer.parseInt(rcIdKat), namarencana.getText().toString(), target_money.getCleanIntValue(), tanggal);
                        onResume();

                        }
                    }
                })

                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    private void select_categoryy() {
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
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(Planning.this, 4);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(SCRVI);
    }

    @SuppressLint("Recycle")
    private void getData2() {
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

    void savePlanning(int IdKategori, String namaplanning, int jumlahtarget, String TanggalTarget) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = Calendar.getInstance().getTime();
        Date date;

        try {
            date = originalFormat.parse(TanggalTarget);
            String set_tgl_target = String.valueOf(targetFormat.format(date));
            String set_tgl_input = targetFormat.format(todayDate);
            SQLiteDatabase create = MyDatabase.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBSLC.Planning.IdKategori, IdKategori);
            values.put(DBSLC.Planning.NamaPlanning, namaplanning);
            values.put(DBSLC.Planning.TargetMoney, jumlahtarget);
            values.put(DBSLC.Planning.TargetDate, set_tgl_target);
            values.put(DBSLC.Planning.DateInput, set_tgl_input);
            create.insert(DBSLC.Planning.NamaTabelPlanning, null, values);

            getData();
            createRV();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
