package topapp.id.app.smartlivingcost.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.maltaisn.calcdialog.CalcDialog;
import com.maltaisn.icondialog.IconHelper;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import topapp.id.app.smartlivingcost.Adapter.SelectCategoryUpdateRVItem;
import topapp.id.app.smartlivingcost.Fragment.DBSLC;
import topapp.id.app.smartlivingcost.Model.KategoriModel;
import topapp.id.app.smartlivingcost.R;

import static android.content.ContentValues.TAG;

public class UpdateDeleteTrans extends AppCompatActivity implements CalcDialog.CalcDialogCallback {
    private DBSLC MyDatabase;
    private List<KategoriModel> myKategori = new ArrayList<>();
    private String rcNamaKat, rcIconKat, rcIdKat, rcJenisKat, photo_uri, tanggal, getidtrans, tanggal_remindat = "";
    public static TabLayout tabLayout;

    @Nullable
    private BigDecimal value = null;
    private NumberFormat nbFmt;
    public TextView namaCat, tv_photo;

    EditText et_biaya, et_tanggal, et_ket, et_remindat;
    ImageView iconcat, iconbiaya, iconket, icontanggal, iconphoto, imvphoto, icon_remindat;

    Toolbar toolbar;
    SimpleDateFormat dateFormatter;
    RecyclerView recyclerView2, recyclerView;
    SelectCategoryUpdateRVItem SCRVI;
    RelativeLayout rlcat;
    Button btn_addTrans;
    TextView btncancel_photo;
    Calendar newCalendar;
    int biaya, tabselect_cat, idTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_trans);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.select_category);
        recyclerView2 = findViewById(R.id.select_category2);
        rlcat = findViewById(R.id.rlcat);
        btn_addTrans = findViewById(R.id.add_trans);
        et_tanggal = findViewById(R.id.et_tanggal);
        iconbiaya = findViewById(R.id.icon_biaya);
        iconket = findViewById(R.id.icon_ket);
        icontanggal = findViewById(R.id.icon_tanggal);
        iconphoto = findViewById(R.id.icon_photo);
        imvphoto = findViewById(R.id.imv_photo);
        btncancel_photo = findViewById(R.id.btncancel_photo);
        tv_photo = findViewById(R.id.tv_photo);
        et_ket = findViewById(R.id.et_ket);
        et_biaya = findViewById(R.id.et_biaya);
        namaCat = findViewById(R.id.tv_catname);
        iconcat = findViewById(R.id.imv_iconcat);
        tabLayout = findViewById(R.id.tabs_select_category);
        icon_remindat = findViewById(R.id.icon_remindat);
        et_remindat = findViewById(R.id.et_remindat);


        ImageView catset = findViewById(R.id.btnsetcat);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String getidcat = b.getString("idcat");
        getidtrans = b.getString("idtrans");
        String getnamacat = b.getString("namacat");
        String geticoncat = b.getString("iconcat");
        String getkettrans = b.getString("kettrans");
        String getphototrans = b.getString("phototrans");
        String getbiaya = b.getString("biaya");
        String gettgl = b.getString("tgl");
        String getjenis = b.getString("jenis");
        String gettanggal_remindat = b.getString("tanggal_remindat");

        rcJenisKat = getjenis;
        rcIconKat = geticoncat;
        rcIdKat = getidcat;
        rcNamaKat = getnamacat;


        photo_uri = getphototrans;
        idTrans = Integer.parseInt(getidtrans);

        setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi
        Objects.requireNonNull(getSupportActionBar()).setTitle("Perbaharui Data");
        toolbar.setNavigationIcon(R.drawable.ic_back); // Set the icon


        MyDatabase = new DBSLC(getBaseContext());


        SCRVI = new SelectCategoryUpdateRVItem(myKategori, idTrans);
        final IconHelper iconHelper = IconHelper.getInstance(this);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        newCalendar = Calendar.getInstance();

        final CalcDialog calcDialog = new CalcDialog();
        Locale localeID = new Locale("in", "ID");
        nbFmt = NumberFormat.getCurrencyInstance(localeID);

        tabLayout.addTab(tabLayout.newTab().setText("Pengeluaran"));
        tabLayout.addTab(tabLayout.newTab().setText("Pemasukan"));

        namaCat.setText(rcNamaKat);
        iconcat.setImageDrawable(iconHelper.getIcon(Integer.parseInt(rcIconKat)).getDrawable(UpdateDeleteTrans.this));
        if (Integer.parseInt(rcJenisKat) == 1) {
            iconcat.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.colorPrimary));
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            getSupportActionBar().setTitle("Perbaharui Data Pemasukan");
            btn_addTrans.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_primary));
            iconbiaya.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.colorPrimary));
            iconket.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.colorPrimary));
            iconphoto.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.colorPrimary));
            icontanggal.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.colorPrimary));
            icon_remindat.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.colorPrimary));


        } else {
            iconcat.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.RedTheme));
            getSupportActionBar().setTitle("Perbaharui Data Pengeluaran");
            toolbar.setBackgroundColor(getResources().getColor(R.color.RedTheme));
            btn_addTrans.setBackground(ContextCompat.getDrawable(this, R.drawable.button_bg_red));
            iconbiaya.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.RedTheme));
            iconket.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.RedTheme));
            iconphoto.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.RedTheme));
            icontanggal.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.RedTheme));
            icon_remindat.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.RedTheme));


        }

        value = (new BigDecimal(Objects.requireNonNull(getbiaya).replaceAll("-", "")));
        biaya = Integer.parseInt(getbiaya.replace("R", "").replace("P", "").replace(".", ""));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format2.parse(gettgl);
            if (!tanggal_remindat.equals("") || !tanggal_remindat.equals("null") || !tanggal_remindat.equals(null) || !tanggal_remindat.isEmpty()) {
                Date date_remindat = format2.parse(gettanggal_remindat);
                tanggal_remindat = dateFormatter.format(date_remindat);
                et_remindat.setText(tanggal_remindat);
            }
            tanggal = dateFormatter.format(date);
            et_tanggal.setText(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        et_ket.setText(getkettrans);
        getData(getjenis);
        et_biaya.setText(nbFmt.format(Integer.parseInt(getbiaya)));
        if (getphototrans.isEmpty()) {

        } else {
            imvphoto.setVisibility(View.VISIBLE);
            Glide.with(this).load(Uri.fromFile(new File(getphototrans))).centerCrop().fitCenter().dontAnimate().error(R.drawable.camera).placeholder(R.drawable.gallery).into(imvphoto);
            btncancel_photo.setVisibility(View.VISIBLE);
        }
        createrv();
        createrv2();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateDeleteTrans.this, MainActivity.class);
                i.putExtra("frgToLoad", "Transaksi");
                // Now start your activity

                startActivity(i);
                UpdateDeleteTrans.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
        });
        SCRVI.setOnItemClickListener(new SelectCategoryUpdateRVItem.ClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                Log.d(TAG, "onItemClick position: " + rcIdKat + rcIconKat + rcNamaKat + rcJenisKat);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        KategoriModel data = myKategori.get(position);
                        rcIconKat = data.getIdIconKat();
                        rcIdKat = data.getIdKat();
                        rcNamaKat = data.getNamaKat();
                        rcJenisKat = data.getJenisKat();
                        namaCat.setText(rcNamaKat);
                        rlcat.setVisibility(View.GONE);

                        iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
                            @Override
                            public void onDataLoaded() {
                                // This happens on UI thread, and is guaranteed to be called.
                                iconcat.setImageDrawable(iconHelper.getIcon(Integer.parseInt(rcIconKat)).getDrawable(UpdateDeleteTrans.this));
                                if (Integer.parseInt(rcJenisKat) == 1) {
                                    iconcat.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.colorPrimary));
                                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    Objects.requireNonNull(getSupportActionBar()).setTitle("Perbaharui Data Pemasukan");
                                    btn_addTrans.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    iconbiaya.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.colorPrimary));
                                    iconket.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.colorPrimary));
                                    iconphoto.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.colorPrimary));
                                    icontanggal.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.colorPrimary));

                                } else {
                                    iconcat.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.RedTheme));
                                    getSupportActionBar().setTitle("Perbaharui Data Pengeluaran");
                                    toolbar.setBackgroundColor(getResources().getColor(R.color.RedTheme));
                                    btn_addTrans.setBackgroundColor(getResources().getColor(R.color.RedTheme));
                                    iconbiaya.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.RedTheme));
                                    iconket.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.RedTheme));
                                    iconphoto.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.RedTheme));
                                    icontanggal.setColorFilter(ContextCompat.getColor(UpdateDeleteTrans.this, R.color.RedTheme));


                                }
                            }
                        });
                    }
                });

            }
        });


        et_biaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rcJenisKat.isEmpty() || rcJenisKat.equals("") || rcJenisKat.equals("null") || rcJenisKat == null) {
                    new SweetAlertDialog(UpdateDeleteTrans.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Menambah Data Gagal")
                            .setContentText("Silahkan Pilih Kategori")
                            .show();
                } else {
                    calcDialog.getSettings().setInitialValue(value)
                            .setNumberFormat(nbFmt).setExpressionEditable(true).setExpressionShown(true);
                    calcDialog.show(getSupportFragmentManager(), "calc_dialog");
                }
            }
        });

        et_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateDeleteTrans.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        et_tanggal.setText(dateFormatter.format(newDate.getTime()));
                        tanggal = dateFormatter.format(newDate.getTime());
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        et_remindat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateDeleteTrans.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        et_remindat.setText(dateFormatter.format(newDate.getTime()));
                        tanggal_remindat = dateFormatter.format(newDate.getTime());
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        iconcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (rlcat.getVisibility() == View.VISIBLE) {
                            rlcat.setVisibility(View.GONE);
                        } else {
                            selectCategory();
                        }
                    }
                });

            }
        });


        final RelativeLayout appBar = findViewById(R.id.appbarv);


        catset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateDeleteTrans.this, Category.class);
                i.putExtra("index", tabselect_cat);
                startActivity(i);
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    cleardatacategory();
                    getData("0");
                    createrv();
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView2.setVisibility(View.GONE);
                    appBar.setBackground(ContextCompat.getDrawable(UpdateDeleteTrans.this, R.drawable.selectcat_bg2));
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.RedTheme));
                    tabselect_cat = 0;
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    cleardatacategory();
                    getData("1");
                    createrv2();
                    recyclerView.setVisibility(View.GONE);
                    recyclerView2.setVisibility(View.VISIBLE);
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    appBar.setBackground(ContextCompat.getDrawable(UpdateDeleteTrans.this, R.drawable.selectcat_bg1));
                    tabselect_cat = 1;
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


        btn_addTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rcJenisKat.isEmpty() || rcJenisKat.equals("") || rcJenisKat.equals("null") || rcJenisKat == null) {
                    new SweetAlertDialog(UpdateDeleteTrans.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Menambah Data Gagal")
                            .setContentText("Silahkan Pilih Kategori")
                            .show();
                } else {
                    if (et_biaya.length() < 1) {
                        new SweetAlertDialog(UpdateDeleteTrans.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Menambah Data Gagal")
                                .setContentText("Silahkan Isi Jumlah Transaksi")
                                .show();
                    } else {
                        updateData();
                    }
                }
            }
        });

        tv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                imvphoto.setImageBitmap(r.getBitmap());
                                imvphoto.setVisibility(View.VISIBLE);
                                btncancel_photo.setVisibility(View.VISIBLE);
                                photo_uri = String.valueOf(r.getPath());
//                                Toast.makeText(UpdateDeleteTrans.this, r.getPath(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                            }
                        }).show(getSupportFragmentManager());
            }
        });

        btncancel_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(UpdateDeleteTrans.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Batalkan Gambar")
                        .setContentText("Apakah Anda Yakin untuk membatalkan gambar terpilih?")
                        .setCancelText("Tidak")
                        .setConfirmText("Ya")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                imvphoto.setVisibility(View.GONE);
                                btncancel_photo.setVisibility(View.GONE);
                                photo_uri = "";
                                sDialog.dismissWithAnimation();
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
        });
    }

    private void updateData() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date, date_remindat;
        String set_remindat = "";
        try {
            date = originalFormat.parse(tanggal);
            if (!tanggal_remindat.equals("") || !tanggal_remindat.isEmpty()) {
                date_remindat = originalFormat.parse(tanggal_remindat);
                set_remindat = String.valueOf(targetFormat.format(date_remindat));
            }
            String set_tgl_transaksi = String.valueOf(targetFormat.format(date));
            String set_idKat = String.valueOf(rcIdKat);
            String set_ket_transaksi = et_ket.getText().toString();
            String set_photo_uri = photo_uri;
            Date todayDate = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String set_tgl_input = formatter.format(todayDate);
            @SuppressLint("SimpleDateFormat") String set_jam_input = new SimpleDateFormat("HH:mm:ss").format(newCalendar.getTime());

            //Mendapatkan Repository dengan Mode Menulis
            SQLiteDatabase database = MyDatabase.getWritableDatabase();

            //Membuat Map Baru, Yang Berisi Nama Kolom dan Data Yang Ingin Dimasukan
            ContentValues values = new ContentValues();
            values.put(DBSLC.Transaksi.IdKat, Integer.parseInt(set_idKat));
            values.put(DBSLC.Transaksi.IdTrans, Integer.parseInt(getidtrans));
            values.put(DBSLC.Transaksi.TglTrans, set_tgl_transaksi);
            values.put(DBSLC.Transaksi.JumlahTrans, biaya);
            values.put(DBSLC.Transaksi.KetTrans, String.valueOf(set_ket_transaksi));
            values.put(DBSLC.Transaksi.GbrTrans, set_photo_uri);
            values.put(DBSLC.Transaksi.TglInputTrans, set_tgl_input);
            values.put(DBSLC.Transaksi.JamInputTrans, set_jam_input);
            if (!tanggal_remindat.equals("") || !tanggal_remindat.isEmpty()) {
                values.put(DBSLC.Transaksi.Remind_At, set_remindat);
            }


            //Menambahkan Baris Baru, Berupa Data Yang Sudah Diinputkan pada Kolom didalam Database
            String selection = DBSLC.Transaksi.IdTrans + " LIKE ?";
            String[] selectionArgs = {getidtrans};
            database.update(DBSLC.Transaksi.NamaTabelTrans, values, selection, selectionArgs);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        new SweetAlertDialog(UpdateDeleteTrans.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Berhasil")
                .setContentText("Sukses Memperbaharui Transaksi")
                .show();
        et_ket.setText("");
        et_biaya.setText("");
        et_tanggal.setText("");

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("frgToLoad", "Transaksi");
        // Now start your activity
        startActivity(i);
        UpdateDeleteTrans.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();


    }

    @Override
    public void onValueEntered(int requestCode, @Nullable BigDecimal value) {
        this.value = value;
        et_biaya.setText(String.valueOf(nbFmt.format(value)));
        if (Integer.parseInt(rcJenisKat) == 0) {
            biaya = -1 * value.intValue();
        } else {
            biaya = value.intValue();
        }

    }


    void selectCategory() {
        rlcat.setVisibility(View.VISIBLE);
        if (rcJenisKat.equals("1")) {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            TabLayout.Tab tab2 = tabLayout.getTabAt(1);
            tab2.select();
        } else {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
            TabLayout.Tab tab2 = tabLayout.getTabAt(0);
            tab2.select();
        }


    }


    //Berisi Statement-Statement Untuk Mengambi Data dari Database
    @SuppressLint("Recycle")
    protected void getData(String id) {
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

    private void cleardatacategory() {
        myKategori.clear();
    }

    private void createrv() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(UpdateDeleteTrans.this, 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(SCRVI);
    }

    private void createrv2() {
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(UpdateDeleteTrans.this, 4);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(SCRVI);

    }

    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("frgToLoad", "Transaksi");
        // Now start your activity
        startActivity(i);
        UpdateDeleteTrans.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_trans, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.delete) {
            deleteTrans();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTrans() {

        new SweetAlertDialog(UpdateDeleteTrans.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Hapus Data")
                .setContentText("Apakah Anda Yakin untuk Menghapus Data Transaksi ini?")
                .setCancelText("Tidak")
                .setConfirmText("Ya")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        //Menghapus Data Dari Database
                        SQLiteDatabase DeleteData = MyDatabase.getWritableDatabase();
                        //Menentukan di mana bagian kueri yang akan dipilih
                        String selection = DBSLC.Transaksi.IdTrans + " LIKE ?";
                        //Menentukan Nama Dari Data Yang Ingin Dihapus
                        String[] selectionArgs = {getidtrans};
                        DeleteData.delete(DBSLC.Transaksi.NamaTabelTrans, selection, selectionArgs);
                        Toast.makeText(UpdateDeleteTrans.this, "Data Transaksi Dihapus", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdateDeleteTrans.this, MainActivity.class);
                        i.putExtra("frgToLoad", "Transaksi");
                        startActivity(i);
                        UpdateDeleteTrans.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
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