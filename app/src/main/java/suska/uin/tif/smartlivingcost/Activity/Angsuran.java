package suska.uin.tif.smartlivingcost.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import suska.uin.tif.smartlivingcost.Adapter.AngsuranRVItem;
import suska.uin.tif.smartlivingcost.Adapter.SelectCategoryRVItem;
import suska.uin.tif.smartlivingcost.Fragment.DBSLC;
import suska.uin.tif.smartlivingcost.Model.AngsuranModel;
import suska.uin.tif.smartlivingcost.Model.KategoriModel;
import suska.uin.tif.smartlivingcost.R;

public class Angsuran extends AppCompatActivity {
    private List<AngsuranModel> myAngsuran = new ArrayList<>();
    private List<KategoriModel> myKategori = new ArrayList<>();
    private DBSLC MyDatabase;
    FloatingActionButton fab;
    RecyclerView recyclerView, recyclerView2;
    TextView tv_nodata;
    String namaplanning, idplannning, idKategori, TargetMoney, TargetDate, getnamakategori, getidiconkategori;

    SimpleDateFormat dateFormatter;
    TextView tv_catname;
    String tanggal, rcIconKat, rcIdKat = "", rcNamaKat, rcJenisKat;
    SelectCategoryRVItem SCRVI;
    RelativeLayout rlcat, appbarv;
    EditText namarencana, target_date;
    CurrencyEditText target_money;
    ImageView imv_iconcat, btnsetcat, icon_nodata;
    IconHelper iconHelper;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angsuran);

        MyDatabase = new DBSLC(getBaseContext());
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler);
        tv_nodata = findViewById(R.id.tv_nodata);
        icon_nodata = findViewById(R.id.icon_nodata);
        mContext = this;

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        if (b != null) {
            namaplanning = b.getString("namaplanning");
            idplannning = b.getString("idplanning");
            idKategori = b.getString("idkategori");
            TargetMoney = b.getString("targetmoney");
            TargetDate = b.getString("targetdate");
            getnamakategori = b.getString("namakategori");
            getidiconkategori = b.getString("idiconkategori");
            rcIdKat = idKategori;

        }

        getSupportActionBar().setTitle(namaplanning);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAngsuran();
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

    private void addAngsuran() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Angsuran.this);
        @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.add_angsuran_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Angsuran.this);
        alertDialogBuilderUserInput.setView(mView);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = Calendar.getInstance().getTime();
        final String set_tgl_input = targetFormat.format(todayDate);

        final CurrencyEditText money_input = mView.findViewById(R.id.money_input);
        money_input.setSeparator(".");
        money_input.setDecimals(false);

        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        SQLiteDatabase database = MyDatabase.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(DBSLC.Angsuran.MoneyInput, money_input.getCleanIntValue());
                        values.put(DBSLC.Angsuran.IdPlanning, Integer.parseInt(idplannning));
                        values.put(DBSLC.Angsuran.DateInput, set_tgl_input);
                        database.insert(DBSLC.Angsuran.NamaTabelAngsuran, null, values);

                        ContentValues values1 = new ContentValues();
                        values1.put(DBSLC.Transaksi.IdKat, Integer.parseInt(idKategori));
                        values1.put(DBSLC.Transaksi.TglTrans, set_tgl_input);
                        values1.put(DBSLC.Transaksi.JumlahTrans, -1 * money_input.getCleanIntValue());
                        values1.put(DBSLC.Transaksi.KetTrans, "Angsuran " + namaplanning);
                        values1.put(DBSLC.Transaksi.GbrTrans, "");
                        values1.put(DBSLC.Transaksi.TglInputTrans, set_tgl_input);
                        values1.put(DBSLC.Transaksi.JamInputTrans, "21.00");

                        //Menambahkan Baris Baru, Berupa Data Yang Sudah Diinputkan pada Kolom didalam Database
                        database.insert(DBSLC.Transaksi.NamaTabelTrans, null, values1);

//                        getData(idplannning);
                        onResume();
                    }
                })

                .setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();


    }

    void createRV(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AngsuranRVItem(myAngsuran);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        ((AngsuranRVItem) adapter).setOnItemClickListener(new AngsuranRVItem.ClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(final int position, View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Angsuran.this);
                @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.add_angsuran_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Angsuran.this);
                alertDialogBuilderUserInput.setView(mView);

                final CurrencyEditText money_input = mView.findViewById(R.id.money_input);
                money_input.setSeparator(".");
                money_input.setDecimals(false);
                TextView dialogTitle = mView.findViewById(R.id.dialogTitle);
                dialogTitle.setText("Perbaharui Angsuran");
                ImageView delete = mView.findViewById(R.id.btn_delete);
                delete.setVisibility(View.VISIBLE);
                money_input.setText(String.valueOf(myAngsuran.get(position).getMoneyInput()));


                alertDialogBuilderUserInput
                        .setCancelable(true)
                        .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                SQLiteDatabase database = MyDatabase.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(DBSLC.Angsuran.MoneyInput, money_input.getCleanIntValue());
                                String selection = DBSLC.Angsuran.IdAngsuran + " LIKE ?";
                                String[] selectionArgs = {String.valueOf(myAngsuran.get(position).getIdAngsuran())};
                                database.update(DBSLC.Angsuran.NamaTabelAngsuran, values, selection, selectionArgs);
//                                getData(idplannning);
                                onResume();
                            }
                        })

                        .setNegativeButton("Batal",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new android.app.AlertDialog.Builder(Angsuran.this, R.style.AlertDialogCustom)
                                .setMessage("Apakah Anda Yakin untuk Menghapus Angsuran Ini?")
                                .setCancelable(true)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Menghapus Data Dari Database
                                        SQLiteDatabase DeleteData = MyDatabase.getWritableDatabase();
                                        //Menentukan di mana bagian kueri yang akan dipilih
                                        String selection = DBSLC.Angsuran.IdAngsuran + " LIKE ?";
                                        //Menentukan Nama Dari Data Yang Ingin Dihapus
                                        String[] selectionArgs = {String.valueOf(myAngsuran.get(position).getIdAngsuran())};
                                        DeleteData.delete(DBSLC.Angsuran.NamaTabelAngsuran, selection, selectionArgs);
                                        alertDialogAndroid.dismiss();
//                                        getData(idplannning);
                                        onResume();
                                    }
                                })
                                .setNegativeButton("Tidak", null)
                                .show();
                    }
                });
            }
        });
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line)));
        recyclerView.addItemDecoration(itemDecoration);
    }

    //Berisi Statement-Statement Untuk Mengambi Data dari Database
    @SuppressLint("Recycle")
    protected void getData(String id) {
        myAngsuran.clear();
        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT * FROM " + DBSLC.Angsuran.NamaTabelAngsuran + " WHERE IdPlanning = '" + id + "'", null);
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
            AngsuranModel angsuranModel = new AngsuranModel(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3));
            myAngsuran.add(angsuranModel);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update_delete_planning, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete:
                new android.app.AlertDialog.Builder(this, R.style.AlertDialogCustom)
                        .setMessage("Apakah Anda Yakin untuk Menghapus Informasi" + namaplanning + " Beserta Seluruh Angsurannya?")
                        .setCancelable(true)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SQLiteDatabase DeleteData = MyDatabase.getWritableDatabase();

                                String selection = DBSLC.Angsuran.IdPlanning + " LIKE ?";

                                String[] selectionArgs = {idplannning};
                                DeleteData.delete(DBSLC.Angsuran.NamaTabelAngsuran, selection, selectionArgs);
                                String selection1 = DBSLC.Planning.IdPlanning + " LIKE ?";

                                String[] selectionArgs1 = {idplannning};
                                DeleteData.delete(DBSLC.Planning.NamaTabelPlanning, selection1, selectionArgs1);
                                finish();

                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();

                return true;
            case R.id.update:
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Angsuran.this);
                @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.planning_master_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Angsuran.this);
                alertDialogBuilderUserInput.setView(mView);


                final Calendar newCalendar = Calendar.getInstance();
                tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                namarencana = mView.findViewById(R.id.nama_rencana);
                target_money = mView.findViewById(R.id.target_money);
                target_money.setSeparator(".");
                target_money.setDecimals(false);
                target_date = mView.findViewById(R.id.target_date);
                imv_iconcat = mView.findViewById(R.id.imv_iconcat);
                tv_catname = mView.findViewById(R.id.tv_catname);
                rlcat = mView.findViewById(R.id.rlcat);
                appbarv = mView.findViewById(R.id.appbarv);
                btnsetcat = mView.findViewById(R.id.btnsetcat);
                iconHelper = IconHelper.getInstance(Angsuran.this);
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                namarencana.setText(namaplanning);
                target_money.setText(TargetMoney);
                target_date.setText(TargetDate);


                recyclerView2 = mView.findViewById(R.id.select_category);

                getData2("0");
                SCRVI = new SelectCategoryRVItem(myKategori);
                SCRVI.notifyDataSetChanged();
                createrv();

                target_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(Angsuran.this, new DatePickerDialog.OnDateSetListener() {
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

                tv_catname.setText(getnamakategori);
                iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
                    @Override
                    public void onDataLoaded() {
                        // This happens on UI thread, and is guaranteed to be called.
                        imv_iconcat.setImageDrawable(iconHelper.getIcon(Integer.parseInt(getidiconkategori)).getDrawable(Angsuran.this));
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
                                        imv_iconcat.setImageDrawable(iconHelper.getIcon(Integer.parseInt(rcIconKat)).getDrawable(Angsuran.this));
                                    }

                                });
                            }
                        });

                    }
                });

                btnsetcat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Angsuran.this, Category.class);
                        i.putExtra("index", 0);
                        startActivity(i);
                    }
                });


                alertDialogBuilderUserInput
                        .setCancelable(true)
                        .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                if (namarencana.getText().toString().equals("") || namarencana.getText().toString().isEmpty()) {
                                    Toast.makeText(Angsuran.this, "Silahkan Isi Nama Perencanaan", Toast.LENGTH_SHORT).show();
                                } else if (Objects.requireNonNull(target_money.getText()).toString().isEmpty() || target_money.getText().toString().equals("")) {
                                    Toast.makeText(Angsuran.this, "Silahkan Isi Jumlah Uang Yang Ingin Dikumpulkan", Toast.LENGTH_SHORT).show();
                                } else if (target_date.getText().toString().equals("") || target_date.getText().toString().isEmpty()) {
                                    Toast.makeText(Angsuran.this, "Silahkan Isi Tanggal Keperluan", Toast.LENGTH_SHORT).show();
                                } else if (rcIdKat.isEmpty()) {
                                    Toast.makeText(Angsuran.this, "Silahkan Isi Pilih Kategori", Toast.LENGTH_SHORT).show();
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        myAngsuran.clear();
        getData(idplannning);
        if (myAngsuran.isEmpty()) {
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
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(Angsuran.this, 4);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(SCRVI);
    }

    @SuppressLint("Recycle")
    private void getData2(String id) {
        myKategori.clear();
        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT * FROM " + DBSLC.MyColumns.NamaTabelKat + " WHERE " + DBSLC.MyColumns.JenisKat + "=" + id, null);
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
            String selection = DBSLC.Planning.IdPlanning + " LIKE ?";
            String[] selectionArgs = {String.valueOf(idplannning)};
            create.update(DBSLC.Planning.NamaTabelPlanning, values, selection, selectionArgs);

            getSupportActionBar().setTitle(namaplanning);


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
